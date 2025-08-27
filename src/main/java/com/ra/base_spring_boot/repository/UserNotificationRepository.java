package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    // Tìm theo user.id và notification.notificationId
    Optional<UserNotification> findByUser_IdAndNotification_NotificationId(Long userId, Long notificationId);

    // Lấy tất cả theo user.id
    List<UserNotification> findAllByUser_Id(Long userId);

    // Lấy tất cả theo notification.notificationId
    List<UserNotification> findAllByNotification_NotificationId(Long notificationId);

    // Xóa theo user.id và notification.notificationId
    void deleteByUser_IdAndNotification_NotificationId(Long userId, Long notificationId);
}
