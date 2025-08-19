package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.NotificationRequest;
import com.ra.base_spring_boot.dto.response.NotificationResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import org.springframework.security.core.Authentication;

public interface NotificationService {
    PaginationResponse<NotificationResponse> getAllByUser(Authentication authentication, int page, int size);

    NotificationResponse create(NotificationRequest request);
    NotificationResponse update(Long id, NotificationRequest request);

    NotificationResponse markAsRead(Long id, Authentication authentication);
    NotificationResponse markAsUnread(Long id, Authentication authentication);

    void markAllAsRead(Authentication authentication);
    void delete(Long id, Authentication authentication);

    PaginationResponse<NotificationResponse> search(Authentication authentication, String keyword, int page, int size);
}
