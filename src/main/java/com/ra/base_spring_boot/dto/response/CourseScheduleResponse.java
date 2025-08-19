package com.ra.base_spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseScheduleResponse {
    private Long id;
    private Long courseOffId;
    private String courseOffName;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
}
