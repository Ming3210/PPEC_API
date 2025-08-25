package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserNotifications_User_Id(Long userId, Pageable pageable);

    Page<Notification> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title, String content, Pageable pageable
    );

    @Query("""
                SELECT n FROM Notification n 
                JOIN n.userNotifications un 
                WHERE un.user.id = :userId
                  AND (LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                       OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Notification> searchByUser(@Param("userId") Long userId,
                                    @Param("keyword") String keyword,
                                    Pageable pageable);

}