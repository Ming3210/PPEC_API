package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "lecturers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank
    @Size(max = 50)
    @Column(name = "lecturer_code", nullable = false, unique = true, length = 50)
    private String lecturerCode;

    @Past
    @Column(name = "date_of_birth")
    private java.time.LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Departments department;

    @Size(max = 100)
    @Column(name = "academic_title", length = 100)
    private String academicTitle;
}

