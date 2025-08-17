package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.NotificationRequest;
import com.ra.base_spring_boot.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getAllByUser(Long userId);
    NotificationResponse create(NotificationRequest request);
    NotificationResponse markAsRead(Long id);
    void delete(Long id);
}