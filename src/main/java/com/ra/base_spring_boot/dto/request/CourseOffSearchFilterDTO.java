package com.ra.base_spring_boot.dto.request;


import com.ra.base_spring_boot.model.constants.TargetAudience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseOffSearchFilterDTO {
    private String name;
    private TargetAudience targetAudience;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private Integer estimatedHoursFrom;
    private Integer estimatedHoursTo;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
    private int page = 0;
    private int size = 10;
}
