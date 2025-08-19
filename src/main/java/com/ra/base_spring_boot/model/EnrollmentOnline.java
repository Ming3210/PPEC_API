package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.StatusEnrollment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentOnline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student không được để trống")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Course không được để trống")
    private Course course;

    @NotNull(message = "Thời gian đăng ký không được để trống")
    private LocalDateTime enrollment_time;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Trạng thái không được để trống")
    private StatusEnrollment status;

    private LocalDateTime completion_time;

    @Min(value = 0, message = "Progress phải >= 0")
    @Max(value = 100, message = "Progress phải <= 100")
    private double progress_percentage;
}
