package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.Level;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String subtitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String provider;

    @Enumerated(EnumType.STRING)
    private Level level;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private BigDecimal rating;

    private Integer ratingCount;

    private Integer studentsCount;

    private String duration;

    private Integer lessonCount;

    private String imageUrl;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public Course(Long id, String code, String title) {
        this.id = id;
        this.code = code;
        this.title = title;
    }

}

