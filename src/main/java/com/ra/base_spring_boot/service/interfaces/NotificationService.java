package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.NotificationRequest;
import com.ra.base_spring_boot.dto.response.NotificationResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;

public interface NotificationService {
    PaginationResponse<NotificationResponse> getAllByUser(Long userId, int page, int size);

    NotificationResponse create(NotificationRequest request);
    NotificationResponse markAsRead(Long id);
    void delete(Long id);
}
