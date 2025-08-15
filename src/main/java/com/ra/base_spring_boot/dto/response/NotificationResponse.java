package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.RoleName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Long notificationId;
    private String title;
    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String fullName;
    private RoleName role;
}
