package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.RoleName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserNotificationResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private RoleName role;
    private Boolean isRead;
    private LocalDateTime readAt;
}
