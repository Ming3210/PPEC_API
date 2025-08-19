package com.ra.base_spring_boot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "study_programs")
@Builder
public class StudyProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private CourseOff courseOff;

    @Column(name = "header_name", length = 250)
    private String headerName;

    @ElementCollection
    @CollectionTable(
            name = "study_program_descriptions",
            joinColumns = @JoinColumn(name = "study_program_id")
    )
    @Column(name = "description", length = 1000)
    private List<String> descriptions;
}
