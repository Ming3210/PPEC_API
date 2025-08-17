package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.RoleName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NewsResponse {
    private Long newsId;
    private String title;
    private String summary;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String fullName;
    private RoleName role;
}
