package com.ra.base_spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseOnlineDTO {
    private Long id;
    private String code;
    private String title;
    private BigDecimal price;
    private String imageUrl;
}
