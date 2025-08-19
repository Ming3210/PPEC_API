package com.ra.base_spring_boot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "centers")
public class Center {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;
    private String logoUrl;

    @Column(
            name = "created_at",
            updatable = false,
            insertable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;
    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CourseOff> coursesOff;
}
