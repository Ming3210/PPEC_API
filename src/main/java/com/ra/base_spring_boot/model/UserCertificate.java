package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.ExamResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_certificates", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "certificateId"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "certificate_id", nullable = false)
    private Long certificateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", length = 20)
    private ExamResult result;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "actual_exam_date")
    private LocalDateTime actualExamDate;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column   (name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "certificate_id", insertable = false, updatable = false)
    private Certificate certificate;
}