package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long studentId;

    // Thông tin User
    private Long userId;
    private String username;
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private String address;
    private String avatarUrl;

    // Thông tin Student
    private String studentCode;
    private String className;

    // Thông tin liên kết
    private Long departmentId;
    private String departmentName;
    private Long industryId;
    private String industryName;
}
