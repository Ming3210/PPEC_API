package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.NotificationRequest;
import com.ra.base_spring_boot.dto.response.NotificationResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.model.Notification;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.repository.NotificationRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository repo;
    @Autowired
    private UserRepository userRepo;

    private User getCurrentUser(Authentication authentication) {
        return userRepo.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user đăng nhập"));
    }

    @Override
    public PaginationResponse<NotificationResponse> getAllByUser(Authentication authentication, int page, int size) {
        User user = getCurrentUser(authentication);

        // ADMIN, SCHOOL_ADMIN có thể xem tất cả
        if (user.getRole().equals("ADMIN") || user.getRole().equals("SCHOOL_ADMIN")) {
            Page<Notification> notiPage = repo.findAll(PageRequest.of(page, size));
            return PaginationResponse.of(notiPage.map(this::mapToResponse));
        }

        // Các role khác chỉ xem của chính mình
        Page<Notification> notiPage = repo.findByUserId(user.getId(), PageRequest.of(page, size));
        return PaginationResponse.of(notiPage.map(this::mapToResponse));
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
    public NotificationResponse update(Long id, NotificationRequest request) {
        Notification noti = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Thông báo không tìm thấy"));

        noti.setTitle(request.getTitle());
        noti.setContent(request.getContent());
        return mapToResponse(repo.save(noti));
    }

    @Override
    public NotificationResponse markAsRead(Long id, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Notification noti = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Thông báo không tìm thấy"));

        if (!user.getRole().equals("ADMIN") && !user.getRole().equals("SCHOOL_ADMIN") &&
                !noti.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Không có quyền truy cập");
        }

        noti.setIsRead(true);
        return mapToResponse(repo.save(noti));
    }

    @Override
    public NotificationResponse markAsUnread(Long id, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Notification noti = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Thông báo không tìm thấy"));

        if (!user.getRole().equals("ADMIN") && !user.getRole().equals("SCHOOL_ADMIN") &&
                !noti.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Không có quyền truy cập");
        }

        noti.setIsRead(false);
        return mapToResponse(repo.save(noti));
    }

    @Override
    public void markAllAsRead(Authentication authentication) {
        User user = getCurrentUser(authentication);

        Page<Notification> notifications;
        if (user.getRole().equals("ADMIN") || user.getRole().equals("SCHOOL_ADMIN")) {
            notifications = repo.findAll(PageRequest.of(0, 10));
        } else {
            notifications = repo.findByUserId(user.getId(), PageRequest.of(0, 10));
        }

        notifications.forEach(n -> n.setIsRead(true));
        repo.saveAll(notifications);
    }

    @Override
    public void delete(Long id, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Notification noti = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Thông báo không tìm thấy"));

        if (!user.getRole().equals("ADMIN") && !user.getRole().equals("SCHOOL_ADMIN") &&
                !noti.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Không có quyền xóa");
        }
        repo.delete(noti);
    }

    @Override
    public PaginationResponse<NotificationResponse> search(Authentication authentication, String keyword, int page, int size) {
        User user = getCurrentUser(authentication);

        Page<Notification> notifications;
        if (user.getRole().equals("ADMIN") || user.getRole().equals("SCHOOL_ADMIN")) {
            notifications = repo.findAll(PageRequest.of(page, size))
                    .map(n -> n);
        } else {
            notifications = repo.findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndContentContainingIgnoreCase(
                    user.getId(), keyword, user.getId(), keyword, PageRequest.of(page, size));
        }

        return PaginationResponse.of(notifications.map(this::mapToResponse));
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
