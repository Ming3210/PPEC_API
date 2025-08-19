package com.ra.base_spring_boot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentRegisterCourseOffRequest {
    @NotNull(message = "CourseOff ID không được để trống")
    private Long courseOffId;
}
