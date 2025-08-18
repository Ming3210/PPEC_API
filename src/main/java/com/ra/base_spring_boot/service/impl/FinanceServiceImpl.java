package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.model.Order;
import com.ra.base_spring_boot.model.Payment;
import com.ra.base_spring_boot.model.Withdrawal;
import com.ra.base_spring_boot.repository.OrderRepository;
import com.ra.base_spring_boot.repository.PaymentRepository;
import com.ra.base_spring_boot.repository.WithdrawalRepository;
import com.ra.base_spring_boot.service.interfaces.IFinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements IFinanceService {

    private final OrderRepository orderRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Map<String, Object> getDashboard(int month, int year) {
        List<Order> orders = orderRepository.findByMonthAndYear(month, year);

        long totalRevenue = orders.stream().mapToLong(Order::getRevenue).sum();
        long offlineRevenue = orders.stream()
                .filter(o -> "Offline".equalsIgnoreCase(o.getCourse().getType()))
                .mapToLong(Order::getRevenue).sum();
        long elearningRevenue = orders.stream()
                .filter(o -> "Elearning".equalsIgnoreCase(o.getCourse().getType()))
                .mapToLong(Order::getRevenue).sum();

        BigDecimal withdrawnAmount = withdrawalRepository.getTotalWithdrawn();
        BigDecimal walletBalance = BigDecimal.valueOf(totalRevenue).subtract(withdrawnAmount);


        List<Map<String, Object>> coursesSold = orders.stream().map(o -> {
            Map<String, Object> m = new HashMap<>();
            m.put("orderId", o.getOrderId());
            m.put("courseName", o.getCourse().getTitle());
            m.put("type", o.getCourse().getType());
            m.put("provider", o.getCourse().getProvider());
            m.put("studentCount", o.getStudentCount());
            m.put("revenue", o.getRevenue());
            return m;
        }).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("totalRevenue", totalRevenue);
        result.put("offlineRevenue", offlineRevenue);
        result.put("elearningRevenue", elearningRevenue);
        result.put("coursesSold", coursesSold);
        result.put("withdrawnAmount", withdrawnAmount);
        result.put("walletBalance", walletBalance);

        return result;
    }

    @Override
    public List<Map<String, Object>> getRevenueDetail(LocalDate startDate,
                                                      LocalDate endDate,
                                                      String keyword) {
        List<Order> orders = orderRepository.searchOrders(startDate, endDate, keyword);

        return orders.stream().map(o -> {
            Map<String, Object> m = new HashMap<>();
            m.put("orderId", o.getOrderId());
            m.put("courseName", o.getCourse().getTitle());
            m.put("type", o.getCourse().getType());
            m.put("studentCount", o.getStudentCount());
            m.put("revenue", o.getRevenue());
            return m;
        }).toList();
    }

        @Override
        public Map<String, Object> getWithdrawalHistory(int page, int size,
                                                        String bankName,
                                                        LocalDateTime startDate,
                                                        LocalDateTime endDate) {
                Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
                Page<Withdrawal> withdrawals = withdrawalRepository.searchWithdrawals(
                        bankName,
                        startDate,
                        endDate,
                        pageable
                );

            BigDecimal totalWithdrawn = withdrawalRepository.getTotalWithdrawn();

            List<Map<String, Object>> transactions = withdrawals.getContent().stream().map(w -> {
                Map<String, Object> m = new HashMap<>();
                m.put("transactionId", w.getTransactionId());
                m.put("bankAccount", w.getBankAccount());
                m.put("bankName", w.getBankName());
                m.put("amount", w.getWithdrawalAmount());
                m.put("date", w.getWithdrawalDate());
                return m;
            }).toList();

            Map<String, Object> result = new HashMap<>();
            result.put("totalWithdrawn", totalWithdrawn);
            result.put("transactions", transactions);
            result.put("pagination", Map.of(
                    "page", page,
                    "totalPages", withdrawals.getTotalPages()
            ));

            return result;
        }

    @Override
    public Map<String, Object> getPaymentHistory(int page, int size,
                                                 LocalDate startDate,
                                                 LocalDate endDate) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("paymentDate").descending());
        Page<Payment> payments = paymentRepository.searchPayments(startDate, endDate, pageable);

        BigDecimal totalPayments = paymentRepository.getTotalRevenue();

        List<Map<String, Object>> paymentList = payments.getContent().stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", p.getPaymentId());
            m.put("courseName", p.getCourse().getTitle());
            m.put("paymentDate", p.getPaymentDate());
            m.put("amount", p.getTotalAmount());
            return m;
        }).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("totalPayments", totalPayments);
        result.put("payments", paymentList);
        result.put("pagination", Map.of(
                "page", page,
                "totalPages", payments.getTotalPages()
        ));

        return result;
    }
}

