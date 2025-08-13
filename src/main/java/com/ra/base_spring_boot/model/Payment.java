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
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(name = "payment_date", updatable = false, insertable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime paymentDate;

    @PrePersist
    public void prePersist() {
        if (this.transactionId == null || this.transactionId.isEmpty()) {
            String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String uuidPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.transactionId = "WD-" + datePart + "-" + uuidPart;
        }
    }
}
