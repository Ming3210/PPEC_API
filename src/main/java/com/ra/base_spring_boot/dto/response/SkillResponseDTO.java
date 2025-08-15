package com.ra.base_spring_boot.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Long totalCourses;
}
