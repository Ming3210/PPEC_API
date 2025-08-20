package com.ra.base_spring_boot.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ra.base_spring_boot.model.constants.Gender;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStudentProfileRequest {

    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;

    private MultipartFile avatar;
    private String avatarUrl;
    private LocalDate dateOfBirth;

    private Gender gender;
    private Long departmentId;
    private String academicYear;
    private Long industryId;
}
