package com.ra.base_spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDTOs {
    private Long id;
    private String name;
    private String description;
    private String status;
    private String type;
}

