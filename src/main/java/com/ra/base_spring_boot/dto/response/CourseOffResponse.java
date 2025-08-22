package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.TargetAudience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseOffResponse {
    private Long id;
    private String name;
    private String bannerUrl;
    private TargetAudience targetAudience;
    private String description;
    private Integer estimatedHours;
    private BigDecimal price;
}
