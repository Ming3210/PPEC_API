package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, Long> {
    boolean existsByTransactionId(String transactionId);
    Payments findByTransactionId(String transactionId);
}
