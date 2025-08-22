package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    Optional<UserNotification> findByUserIdAndNotification_NotificationId(Long userId, Long notificationId);

    List<UserNotification> findAllByUserId(Long userId);

    List<UserNotification> findAllByNotification_NotificationId(Long notificationId);

    void deleteByUserIdAndNotification_NotificationId(Long userId, Long notificationId);
}
