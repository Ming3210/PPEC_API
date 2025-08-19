package com.ra.base_spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseOfflineDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String bannerUrl;
    private Integer estimatedHours;
}
