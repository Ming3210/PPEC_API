package com.ra.base_spring_boot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentCourseOffRequest {
    @NotNull(message = "Student ID không được để trống")
    private Long studentId;

    @NotNull(message = "CourseOff ID không được để trống")
    private Long courseOffId;

}