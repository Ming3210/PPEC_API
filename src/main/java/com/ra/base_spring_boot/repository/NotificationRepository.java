package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Notification;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.constants.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE LOWER(n.role) LIKE LOWER(CONCAT('%', :role, '%'))")
    Page<Notification> findByRoleContains(String role, Pageable pageable);

//    Page<Notification> findByRoleContainingIgnoreCase(String role, Pageable pageable);

    Page<Notification> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title, String content, Pageable pageable
    );

    @Query("""
        SELECT n 
        FROM Notification n 
        JOIN n.userNotifications un 
        WHERE un.user.id = :userId
          AND (:keyword IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<Notification> searchByUser(@Param("userId") Long userId,
                                    @Param("keyword") String keyword,
                                    Pageable pageable);
}