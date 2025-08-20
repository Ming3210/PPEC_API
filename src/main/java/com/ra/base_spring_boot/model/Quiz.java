package com.ra.base_spring_boot.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String duration;

    private Integer totalQuestions;

    private Integer attemptsAllowed;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<Question> questions;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;


    public Quiz(Long id, String title) {
        this.id = id;
        this.title = title;
    }

}

