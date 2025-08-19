package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(columnDefinition = "TEXT")
    private String questionText;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column(columnDefinition = "TEXT")
    private List<String> options;

    private String answer;

    private String correctAnswer;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "order_number")
    private Integer orderNumber;

    private LocalDateTime createdAt;


    public Question(Long id, String questionText, Quiz quiz) {
        this.id = id;
        this.questionText = questionText;
        this.quiz = quiz;
    }

}
