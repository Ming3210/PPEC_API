package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Withdrawal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    @Query("SELECT w FROM Withdrawal w WHERE " +
            "(:bankName IS NULL OR w.bankName LIKE %:bankName%) AND " +
            "(:fromDate IS NULL OR w.withdrawalDate >= :fromDate) AND " +
            "(:toDate IS NULL OR w.withdrawalDate <= :toDate)")
    Page<Withdrawal> searchWithdrawals(@Param("bankName") String bankName,
                                       @Param("fromDate") LocalDateTime fromDate,
                                       @Param("toDate") LocalDateTime toDate,
                                       Pageable pageable);

    @Query("SELECT COALESCE(SUM(w.withdrawalAmount), 0) FROM Withdrawal w")
    BigDecimal getTotalWithdrawn();
}