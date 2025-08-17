package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.NotificationRequest;
import com.ra.base_spring_boot.dto.response.NotificationResponse;
import com.ra.base_spring_boot.model.Notification;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.repository.NotificationRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository repo;
    @Autowired
    private UserRepository userRepo;

    @Override
    public List<NotificationResponse> getAllByUser(Long userId) {
        return repo.findByUserId(userId)
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Override
    public NotificationResponse create(NotificationRequest request) {
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Notification noti = new Notification();
        noti.setTitle(request.getTitle());
        noti.setContent(request.getContent());
        noti.setIsRead(false);
        noti.setUser(user);

        return mapToResponse(repo.save(noti));
    }

    @Override
    public NotificationResponse markAsRead(Long id) {
        Notification noti = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Thông báo không tìm thấy"));
        noti.setIsRead(true);
        return mapToResponse(repo.save(noti));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    private NotificationResponse mapToResponse(Notification n) {
        return NotificationResponse.builder()
            .notificationId(n.getNotificationId())
            .title(n.getTitle())
            .content(n.getContent())
            .isRead(n.getIsRead())
            .createdAt(n.getCreatedAt())
            .userId(n.getUser().getId())
            .build();
    }
}