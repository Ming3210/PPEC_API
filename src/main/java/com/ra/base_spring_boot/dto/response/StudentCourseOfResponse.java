package com.ra.base_spring_boot.dto.response;

import lombok.*;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentCourseOfResponse {
    private Long courseOffId;
    private String courseOffName;
    private LocalDateTime registrationDate;
    private String status;
}
