package com.ra.base_spring_boot.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentCourseOffResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long courseOffId;
    private String courseOffName;
    private LocalDateTime registrationDate;
    private String status;
}
