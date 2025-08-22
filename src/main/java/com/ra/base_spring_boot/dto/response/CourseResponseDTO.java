package com.ra.base_spring_boot.dto.response;


import com.ra.base_spring_boot.model.constants.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {
    private Long id;
    private String code;
    private String title;
    private String subtitle;
    private String description;
    private String provider;
    private Level level;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Double rating;
    private Integer ratingCount;
    private Integer studentsCount;
    private String duration;
    private Integer lessonCount;
    private String imageUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long partnerId;
    private String partnerName;
}
