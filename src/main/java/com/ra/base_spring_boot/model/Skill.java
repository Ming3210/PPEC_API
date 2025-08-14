package com.ra.base_spring_boot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"courses"})
@EqualsAndHashCode(exclude = {"courses"})
@Entity
@Table(name = "skills")
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(
            name = "created_at",
            updatable = false,
            insertable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "skills")
    private Set<CourseOff> courses = new HashSet<>();

    public Skill(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
