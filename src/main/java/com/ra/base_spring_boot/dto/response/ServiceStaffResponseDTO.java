package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.AccountStatus;
import com.ra.base_spring_boot.model.constants.RoleName;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceStaffResponseDTO {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private RoleName role;
    private AccountStatus status;
    private LocalDateTime createdAt;

    private LocalDate dateOfBirth;
    private String hometown;
    private String avatarUrl;
    private String position;
    private String staffServiceCode;

    private Long partnerId;
    private String partnerName;
}

