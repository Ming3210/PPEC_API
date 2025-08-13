package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.SubmissionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quiz_submissions")
public class QuizSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private String studentId;

    @Column(columnDefinition = "TEXT")
    private String answers;

    private Integer score;

    private Integer totalQuestions;

    private Integer correctAnswers;

    private Integer timeSpent;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    private LocalDateTime submittedAt;

    public QuizSubmission(Long id, Quiz quiz, String studentId) {
        this.id = id;
        this.quiz = quiz;
        this.studentId = studentId;
    }

}
