package com.ra.base_spring_boot.service.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IFinanceService {
    Map<String, Object> getDashboard(int month, int year);

    List<Map<String, Object>> getRevenueDetail(LocalDate startDate,
                                               LocalDate endDate,
                                               String keyword);

    Map<String, Object> getWithdrawalHistory(int page, int size,
                                             String bankName,
                                             LocalDateTime startDate,
                                             LocalDateTime endDate);

    Map<String, Object> getPaymentHistory(int page, int size,
                                          LocalDate startDate,
                                          LocalDate endDate);
}
