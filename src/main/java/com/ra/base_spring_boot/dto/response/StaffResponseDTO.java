package com.ra.base_spring_boot.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffResponseDTO {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String hometown;
    private String avatarUrl;

    // Staff info
    private String employeeCode;
    private Integer startYear;
    private String position;


    private Long schoolId;
    private String schoolName;

    private String role;
    private String status;
}

