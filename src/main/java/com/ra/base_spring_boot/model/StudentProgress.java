package com.ra.base_spring_boot.model;

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
@Table(name = "student_progress")
public class StudentProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private Integer watchTime;

    private Double completionPercentage;

    private Boolean isCompleted;

    private LocalDateTime lastAccessed;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public StudentProgress(Long id, String studentId, Course course, Lesson lesson) {
        this.id = id;
        this.studentId = studentId;
        this.lesson = lesson;
    }

}
