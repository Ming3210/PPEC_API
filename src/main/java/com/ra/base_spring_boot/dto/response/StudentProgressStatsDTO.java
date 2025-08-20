package com.ra.base_spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProgressStatsDTO {
    private double attendanceRate;
    private double assignmentRate;
    private double preparationRate;
}

