package com.ra.base_spring_boot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "withdrawals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawalId;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "bank_account", nullable = false)
    private String bankAccount;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "withdrawal_amount", precision = 15, scale = 0)
    private BigDecimal withdrawalAmount;

    @Column(name = "withdrawal_date", updatable = false, insertable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime withdrawalDate;

    @PrePersist
    public void prePersist() {
        if (this.transactionId == null || this.transactionId.isEmpty()) {
            String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String uuidPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.transactionId = "WD-" + datePart + "-" + uuidPart;
        }
    }
}
