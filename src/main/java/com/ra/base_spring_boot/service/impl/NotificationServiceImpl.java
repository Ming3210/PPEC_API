package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.NotificationRequest;
import com.ra.base_spring_boot.dto.response.NotificationResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.UserNotificationResponse;
import com.ra.base_spring_boot.model.Notification;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.UserNotification;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.NotificationRepository;
import com.ra.base_spring_boot.repository.UserNotificationRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired private NotificationRepository notificationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserNotificationRepository userNotificationRepository;

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user đăng nhập"));
    }

    private boolean isAdminOrSchoolAdmin(User user) {
        return user.getRole() == RoleName.ADMIN || user.getRole() == RoleName.CENTER;
    }

    /** Lấy danh sách thông báo theo NGƯỜI DÙNG (từng bản ghi UserNotification) */
    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<NotificationResponse> getAllByUser(Authentication authentication, int page, int size) {
        User user = getCurrentUser(authentication);

        if (isAdminOrSchoolAdmin(user)) {
            Page<Notification> pageData = notificationRepository.findAll(PageRequest.of(page, size));
            return PaginationResponse.of(pageData.map(this::mapForAdmin));
        } else {
            // lấy tất cả notification có chứa role của user
            Page<Notification> pageData = notificationRepository.findByRoleContains(
                    user.getRole().name(),
                    PageRequest.of(page, size)
            );
            return PaginationResponse.of(pageData.map(n -> mapForUser(n, user)));
        }
    }

    /** Tạo 1 notification và gán cho N user */
    @Override
    @Transactional
    public NotificationResponse create(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());

        Notification saved = notificationRepository.save(notification);

        List<UserNotification> links = new ArrayList<>();

        // Nếu chọn roles → gửi cho tất cả user có role đó
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            List<User> users = userRepository.findAllByRoleIn(request.getRoles());
            for (User u : users) {
                links.add(UserNotification.builder()
                        .notification(saved)
                        .user(u)
                        .isRead(false)
                        .build());
            }
        }

        userNotificationRepository.saveAll(links);
        saved.setUserNotifications(links);

        return mapForAdmin(saved);
    }


    /** Cập nhật tiêu đề/nội dung; nếu request có userIds thì thay danh sách người nhận */
    @Override
    @Transactional
    public NotificationResponse update(Long id, NotificationRequest request) {
        Notification noti = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thông báo không tìm thấy"));

        // Cập nhật title, content
        noti.setTitle(request.getTitle());
        noti.setContent(request.getContent());
        Notification saved = notificationRepository.save(noti);

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            // Xóa danh sách người nhận cũ
            List<UserNotification> oldLinks = userNotificationRepository.findAllByNotification_NotificationId(id);
            userNotificationRepository.deleteAll(oldLinks);

            // Lấy user theo role mới
            List<User> users = userRepository.findAllByRoleIn(request.getRoles());

            List<UserNotification> newLinks = new ArrayList<>();
            for (User u : users) {
                newLinks.add(UserNotification.builder()
                        .notification(saved)
                        .user(u)
                        .isRead(false) // reset trạng thái đọc
                        .build());
            }
            userNotificationRepository.saveAll(newLinks);
            saved.setUserNotifications(newLinks);
        }

        return mapForAdmin(saved);
    }

    /** Đánh dấu đã đọc: áp dụng CHO BẢN GHI CỦA NGƯỜI ĐANG ĐĂNG NHẬP */
    @Override
    @Transactional
    public NotificationResponse markAsRead(Long notificationId, Authentication authentication) {
        User user = getCurrentUser(authentication);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));

        // kiểm tra role: chỉ cho phép đọc nếu role user nằm trong roles của notification
        if (notification.getRole() == null || !notification.getRole().contains(user.getRole())) {
            throw new RuntimeException("Bạn không có quyền xem thông báo này");
        }

        // Tìm link hoặc tạo mới
        UserNotification link = userNotificationRepository
                .findByUser_IdAndNotification_NotificationId(user.getId(), notificationId)
                .orElseGet(() -> UserNotification.builder()
                        .user(user)
                        .notification(notification)
                        .isRead(false)
                        .build()
                );

        link.setIsRead(true);
        link.setReadAt(LocalDateTime.now());
        userNotificationRepository.save(link);

        return mapForUser(link.getNotification(), user);
    }



    /** Đánh dấu chưa đọc: áp dụng CHO BẢN GHI CỦA NGƯỜI ĐANG ĐĂNG NHẬP */
    @Override
    @Transactional
    public NotificationResponse markAsUnread(Long notificationId, Authentication authentication) {
        User user = getCurrentUser(authentication);

        UserNotification link = userNotificationRepository
                .findByUser_IdAndNotification_NotificationId(user.getId(), notificationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo thuộc về bạn"));

        link.setIsRead(false);
        userNotificationRepository.save(link);

        return mapForUser(link.getNotification(), user);
    }

    /** Đánh dấu tất cả đã đọc: CHO NGƯỜI ĐANG ĐĂNG NHẬP */
    @Override
    @Transactional
    public void markAllAsRead(Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<UserNotification> links = userNotificationRepository.findAllByUser_Id(user.getId());
        links.forEach(l -> { l.setIsRead(true); l.setReadAt(LocalDateTime.now()); });
        userNotificationRepository.saveAll(links);
    }

    /** Xóa:
     *  - ADMIN/SCHOOL_ADMIN: xóa cả notification (mọi người nhận bị xóa theo)
     *  - user thường: chỉ xóa bản ghi của chính mình (unsub thông báo đó)
     */
    @Override
    @Transactional
    public void delete(Long notificationId, Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (isAdminOrSchoolAdmin(user)) {
            Notification noti = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new RuntimeException("Thông báo không tìm thấy"));
            notificationRepository.delete(noti);
        } else {
            // xóa liên kết của riêng user
            userNotificationRepository.deleteByUser_IdAndNotification_NotificationId(user.getId(), notificationId);
        }
    }

    /** Tìm kiếm theo tiêu đề/nội dung:
     *  - ADMIN/SCHOOL_ADMIN: search toàn hệ thống (trả về per-user entries)
     *  - user thường: search trong thông báo của chính mình
     */
    @Override
    public PaginationResponse<NotificationResponse> search(Authentication authentication, String keyword, int page, int size) {
        User user = getCurrentUser(authentication);
        Page<Notification> pageData;

        if (isAdminOrSchoolAdmin(user)) {
            pageData = notificationRepository
                    .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, PageRequest.of(page, size));
            return PaginationResponse.of(pageData.map(this::mapForAdmin));
        } else {
            pageData = notificationRepository.searchByUser(user.getId(), keyword, PageRequest.of(page, size));
            return PaginationResponse.of(pageData.map(n -> mapForUser(n, user)));
        }
    }

    /** Map cho Admin/Center: gồm danh sách tất cả userNotifications */
    private NotificationResponse mapForAdmin(Notification n) {
        return NotificationResponse.builder()
                .notificationId(n.getNotificationId())
                .title(n.getTitle())
                .content(n.getContent())
                .createdAt(n.getCreatedAt())
                .updatedAt(n.getUpdatedAt())
                .userNotifications(
                        n.getUserNotifications() == null ? List.of() :
                                n.getUserNotifications().stream()
                                        .map(un -> UserNotificationResponse.builder()
                                                .id(un.getId())
                                                .userId(un.getUser().getId())
                                                .fullName(un.getUser().getFullName())
                                                .role(un.getUser().getRole())
                                                .isRead(un.getIsRead())
                                                .readAt(un.getReadAt())
                                                .build()
                                        ).toList()
                )
                .build();
    }

    /** Map cho User thường: chỉ hiện trạng thái đọc của chính user */
    private NotificationResponse mapForUser(Notification n, User user) {
        UserNotification link = userNotificationRepository
                .findByUser_IdAndNotification_NotificationId(user.getId(), n.getNotificationId())
                .orElse(null);

        return NotificationResponse.builder()
                .notificationId(n.getNotificationId())
                .title(n.getTitle())
                .content(n.getContent())
                .createdAt(n.getCreatedAt())
                .updatedAt(n.getUpdatedAt())
                .isRead(link != null && Boolean.TRUE.equals(link.getIsRead()))
                .readAt(link != null ? link.getReadAt() : null)
                .build();
    }
}
