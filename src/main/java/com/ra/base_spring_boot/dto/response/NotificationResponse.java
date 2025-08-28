package com.ra.base_spring_boot.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class NotificationResponse {
    private Long notificationId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // trạng thái đọc của user hiện tại
    private Boolean isRead;
    private LocalDateTime readAt;

    // chỉ dùng khi admin muốn xem tất cả user
    private List<UserNotificationResponse> userNotifications;
}

