package com.ra.base_spring_boot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "teaching_assistants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachingAssistant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank
    @Size(max = 50)
    @Column(name = "ta_code", nullable = false, unique = true, length = 50)
    private String taCode;
    private String avatar;
    @ManyToOne
    @JoinColumn(name = "assigned_lecturer_id")
    private Lecturer assignedLecturer;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Departments department;
}
