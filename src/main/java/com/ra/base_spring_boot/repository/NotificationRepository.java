package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
//    Page<Notification> findByUserId(Long userId, Pageable pageable);
//    Page<Notification> findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndContentContainingIgnoreCase(
//            Long userId1, String titleKeyword,
//            Long userId2, String contentKeyword,
//            Pageable pageable
//    );
}