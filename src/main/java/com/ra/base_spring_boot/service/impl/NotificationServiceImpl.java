package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.NotificationRequest;
import com.ra.base_spring_boot.dto.response.NotificationResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.model.Notification;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.UserNotification;
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
        return "ADMIN".equals(user.getRole().name()) || "SCHOOL_ADMIN".equals(user.getRole().name());
    }

    /** Lấy danh sách thông báo theo NGƯỜI DÙNG (từng bản ghi UserNotification) */
    @Override
    public PaginationResponse<NotificationResponse> getAllByUser(Authentication authentication, int page, int size) {
        User user = getCurrentUser(authentication);

        Page<UserNotification> pageData = isAdminOrSchoolAdmin(user)
                ? userNotificationRepository.findAll(PageRequest.of(page, size))
                : userNotificationRepository.findByUserId(user.getId(), PageRequest.of(page, size));

        return PaginationResponse.of(pageData.map(this::mapToResponse));
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
        for (Long uid : request.getUserIds()) {
            User u = userRepository.findById(uid)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy user id: " + uid));
            links.add(UserNotification.builder()
                    .notification(saved)
                    .user(u)
                    .isRead(false)
                    .build());
        }
        userNotificationRepository.saveAll(links);

        // Trả về "thông tin thông báo" (không gắn user cụ thể) -> các field user sẽ null
        return mapToResponse(saved);
    }

    /** Cập nhật tiêu đề/nội dung; nếu request có userIds thì thay danh sách người nhận */
    @Override
    @Transactional
    public NotificationResponse update(Long id, NotificationRequest request) {
        Notification noti = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thông báo không tìm thấy"));

        noti.setTitle(request.getTitle());
        noti.setContent(request.getContent());
        Notification saved = notificationRepository.save(noti);

        if (request.getUserIds() != null) {
            // Xóa danh sách hiện tại và gán lại
            List<UserNotification> oldLinks = userNotificationRepository.findAllByNotification_NotificationId(id);
            userNotificationRepository.deleteAll(oldLinks);

            List<UserNotification> newLinks = new ArrayList<>();
            for (Long uid : request.getUserIds()) {
                User u = userRepository.findById(uid)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy user id: " + uid));
                newLinks.add(UserNotification.builder()
                        .notification(saved)
                        .user(u)
                        .isRead(false) // reset trạng thái đọc khi thay người nhận
                        .build());
            }
            userNotificationRepository.saveAll(newLinks);
        }

        return mapToResponse(saved);
    }

    /** Đánh dấu đã đọc: áp dụng CHO BẢN GHI CỦA NGƯỜI ĐANG ĐĂNG NHẬP */
    @Override
    @Transactional
    public NotificationResponse markAsRead(Long notificationId, Authentication authentication) {
        User user = getCurrentUser(authentication);

        UserNotification link = userNotificationRepository
                .findByUserIdAndNotification_NotificationId(user.getId(), notificationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo thuộc về bạn"));

        link.setIsRead(true);
        link.setReadAt(LocalDateTime.now());
        return mapToResponse(userNotificationRepository.save(link));
    }

    /** Đánh dấu chưa đọc: áp dụng CHO BẢN GHI CỦA NGƯỜI ĐANG ĐĂNG NHẬP */
    @Override
    @Transactional
    public NotificationResponse markAsUnread(Long notificationId, Authentication authentication) {
        User user = getCurrentUser(authentication);

        UserNotification link = userNotificationRepository
                .findByUserIdAndNotification_NotificationId(user.getId(), notificationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo thuộc về bạn"));

        link.setIsRead(false);
        link.setReadAt(null);
        return mapToResponse(userNotificationRepository.save(link));
    }

    /** Đánh dấu tất cả đã đọc: CHO NGƯỜI ĐANG ĐĂNG NHẬP */
    @Override
    @Transactional
    public void markAllAsRead(Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<UserNotification> links = userNotificationRepository.findAllByUserId(user.getId());
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
            userNotificationRepository.deleteByUserIdAndNotification_NotificationId(user.getId(), notificationId);
        }
    }

    /** Tìm kiếm theo tiêu đề/nội dung:
     *  - ADMIN/SCHOOL_ADMIN: search toàn hệ thống (trả về per-user entries)
     *  - user thường: search trong thông báo của chính mình
     */
    @Override
    public PaginationResponse<NotificationResponse> search(Authentication authentication, String keyword, int page, int size) {
        User user = getCurrentUser(authentication);

        Page<UserNotification> pageData = isAdminOrSchoolAdmin(user)
                ? userNotificationRepository.findByNotification_TitleContainingIgnoreCaseOrNotification_ContentContainingIgnoreCase(
                keyword, keyword, PageRequest.of(page, size))
                : userNotificationRepository
                .findByUserIdAndNotification_TitleContainingIgnoreCaseOrUserIdAndNotification_ContentContainingIgnoreCase(
                        user.getId(), keyword, user.getId(), keyword, PageRequest.of(page, size)
                );

        return PaginationResponse.of(pageData.map(this::mapToResponse));
    }

    /* --------- Mapper ---------- */

    /** Map per-user record -> response đầy đủ */
    private NotificationResponse mapToResponse(UserNotification un) {
        return NotificationResponse.builder()
                .notificationId(un.getNotification().getNotificationId())
                .title(un.getNotification().getTitle())
                .content(un.getNotification().getContent())
                .isRead(un.getIsRead())
                .createdAt(un.getNotification().getCreatedAt())
                .updatedAt(un.getNotification().getUpdatedAt())
                .userId(un.getUser().getId())
                .fullName(un.getUser().getFullName())
                .role(un.getUser().getRole())
                .build();
    }

    /** Map notification tổng quan (dùng khi tạo/cập nhật; các field user sẽ null) */
    private NotificationResponse mapToResponse(Notification n) {
        return NotificationResponse.builder()
                .notificationId(n.getNotificationId())
                .title(n.getTitle())
                .content(n.getContent())
                .isRead(null)
                .createdAt(n.getCreatedAt())
                .updatedAt(n.getUpdatedAt())
                .userId(null)
                .fullName(null)
                .role(null)
                .build();
    }
}
