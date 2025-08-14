package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.until.ExamStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "exams")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    @Column(nullable = false, unique = true)
    private String examCode;

    @Column(nullable = false)
    private String title;

    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamStatus status;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;
}

