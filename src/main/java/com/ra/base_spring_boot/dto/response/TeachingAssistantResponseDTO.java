package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.AccountStatus;
import com.ra.base_spring_boot.model.constants.RoleName;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachingAssistantResponseDTO {
    
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private RoleName role;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long taId;
    private String taCode;
    private String assignedLecturerName;
    private String departmentName;
}

