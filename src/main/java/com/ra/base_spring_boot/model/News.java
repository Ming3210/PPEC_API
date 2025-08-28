package com.ra.base_spring_boot.model;

import com.ra.base_spring_boot.model.constants.NewsStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "thumbnail_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private NewsStatus status = NewsStatus.PENDING;

    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "update_at", insertable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
