package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT COALESCE(SUM(p.totalAmount), 0) FROM Payment p")
    BigDecimal getTotalRevenue();

    @Query("SELECT p.course.title, COUNT(p), SUM(p.totalAmount) " +
            "FROM Payment p GROUP BY p.course.title")
    List<Object[]> getRevenueByCourse();
    @Query("SELECT p FROM Payment p " +
            "WHERE (:startDate IS NULL OR p.paymentDate >= :startDate) " +
            "AND (:endDate IS NULL OR p.paymentDate <= :endDate)")
    Page<Payment> searchPayments(LocalDate startDate,
                                 LocalDate endDate,
                                 Pageable pageable);

}