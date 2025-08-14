package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.LessonType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String videoUrl;

    private Integer duration;

    @Column(name = "order_number")
    private Integer orderNumber;

    @Enumerated(EnumType.STRING)
    private LessonType type;

    private Boolean isFree;

    @Column(columnDefinition = "TEXT")
    private String resources;

    private LocalDateTime createdAt;

    public Lesson(Long id, String title, Course course) {
        this.id = id;
        this.title = title;
        this.course = course;
    }

}
