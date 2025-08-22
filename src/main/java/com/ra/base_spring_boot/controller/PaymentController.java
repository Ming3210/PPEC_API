package com.ra.base_spring_boot.controller;

import com.paypal.base.rest.PayPalRESTException;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.request.PaymentRequestDTO;
import com.ra.base_spring_boot.dto.response.PaymentResponseDTO;
import com.ra.base_spring_boot.service.interfaces.IPayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPayPalService payPalPaymentService;

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<PaymentResponseDTO>> createPaypalPayment(
            @RequestBody PaymentRequestDTO dto) throws PayPalRESTException {
        PaymentResponseDTO payment = payPalPaymentService.createPaypalPayment(dto);
        return ResponseEntity.ok(ResponseWrapper.<PaymentResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(payment)
                .build());
    }

    @GetMapping("/success")
    public ResponseEntity<ResponseWrapper<String>> executePaypalPayment(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId) throws PayPalRESTException {
        String state = payPalPaymentService.executePaypalPayment(paymentId, payerId);
        return ResponseEntity.ok(ResponseWrapper.<String>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data("Payments state: " + state)
                .build());
    }

    @GetMapping("/cancel")
    public ResponseEntity<ResponseWrapper<String>> cancelPaypalPayment() {
        return ResponseEntity.ok(ResponseWrapper.<String>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data("Payments cancelled or failed")
                .build());
    }
}
