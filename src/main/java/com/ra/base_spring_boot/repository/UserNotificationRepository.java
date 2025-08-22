package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.UserNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    Page<UserNotification> findByUserId(Long userId, Pageable pageable);

    Page<UserNotification> findByUserIdAndNotification_TitleContainingIgnoreCaseOrUserIdAndNotification_ContentContainingIgnoreCase(
            Long userId1, String titleKeyword,
            Long userId2, String contentKeyword,
            Pageable pageable
    );

    Page<UserNotification> findByNotification_TitleContainingIgnoreCaseOrNotification_ContentContainingIgnoreCase(
            String titleKeyword, String contentKeyword, Pageable pageable
    );

    Optional<UserNotification> findByUserIdAndNotification_NotificationId(Long userId, Long notificationId);

    List<UserNotification> findAllByUserId(Long userId);

    List<UserNotification> findAllByNotification_NotificationId(Long notificationId);

    void deleteByUserIdAndNotification_NotificationId(Long userId, Long notificationId);
}
