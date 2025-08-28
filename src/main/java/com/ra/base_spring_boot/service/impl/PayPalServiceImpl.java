package com.ra.base_spring_boot.service.impl;

import com.paypal.api.payments.*;
import com.ra.base_spring_boot.dto.request.PaymentRequestDTO;
import com.ra.base_spring_boot.dto.response.PaymentResponseDTO;
import com.ra.base_spring_boot.model.Course;
import com.ra.base_spring_boot.model.Payments;
import com.ra.base_spring_boot.repository.CourseRepository;
import com.ra.base_spring_boot.repository.PaymentRepository;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.ra.base_spring_boot.service.interfaces.IPayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements IPayPalService {

    private final APIContext apiContext;
    private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;

    @Value("${paypal.success-url:http://localhost:8080/api/payments/success}")
    private String successUrl;

    @Value("${paypal.cancel-url:http://localhost:8080/api/payments/cancel}")
    private String cancelUrl;

    @Override
    public PaymentResponseDTO createPaypalPayment(PaymentRequestDTO dto) throws PayPalRESTException {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        String currency = dto.getCurrency();
        if (currency == null || currency.trim().isEmpty()) {
            currency = "USD";
        }

        BigDecimal price = course.getPrice();
        if (price == null) {
            throw new IllegalArgumentException("Giá khóa học không hợp lệ");
        }

        Amount amount = new Amount();
        amount.setCurrency(currency);

        String totalStr = price.setScale(2, RoundingMode.HALF_UP).toPlainString();
        amount.setTotal(totalStr);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(dto.getDescription() != null ? dto.getDescription() : "Payment for course: " + course.getTitle());
        transaction.setCustom(String.valueOf(course.getId()));

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        Payment created = payment.create(apiContext);

        String approvalLink = created.getLinks().stream()
                .filter(link -> "approval_url".equalsIgnoreCase(link.getRel()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No PayPal approval_url found"))
                .getHref();

        return new PaymentResponseDTO(approvalLink, created.getId());
    }

    @Override
    public String executePaypalPayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        Payment executedPayment = payment.execute(apiContext, paymentExecution);

        if ("approved".equalsIgnoreCase(executedPayment.getState())) {
            Transaction tx = executedPayment.getTransactions().get(0);
            Amount amount = tx.getAmount();

            // Lấy courseId từ transaction custom field
            Long courseId = Long.valueOf(tx.getCustom());
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if (!paymentRepository.existsByTransactionId(paymentId)) {
                Payments payEntity = Payments.builder()
                        .transactionId(paymentId)
                        .course(course)
                        .totalAmount(course.getPrice())
                        .paymentDate(LocalDateTime.now())
                        .build();
                paymentRepository.save(payEntity);
            }
        }
        return executedPayment.getState();
    }
}
