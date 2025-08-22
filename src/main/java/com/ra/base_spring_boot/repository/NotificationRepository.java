package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT DISTINCT n FROM Notification n JOIN n.userNotifications un WHERE un.user.id = :userId")
    Page<Notification> findByUserId(@Param("userId") Long userId, Pageable pageable);

    Page<Notification> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title, String content, Pageable pageable
    );

    Page<Notification> findByUserNotifications_User_IdAndTitleContainingIgnoreCaseOrUserNotifications_User_IdAndContentContainingIgnoreCase(
            Long userId1, String titleKeyword,
            Long userId2, String contentKeyword,
            Pageable pageable
    );
}