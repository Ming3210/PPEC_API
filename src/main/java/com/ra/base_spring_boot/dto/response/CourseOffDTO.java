package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.Center;
import com.ra.base_spring_boot.model.constants.TargetAudience;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Data
@Builder
public class CourseOffDTO {
    private String name;

    private String bannerUrl;

    private TargetAudience targetAudience;

    private String description;

    private Integer estimatedHours;

    private BigDecimal price;

    private Set<String> skills;
}
