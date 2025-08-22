package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExamScheduleDTO {
    private String examCode;

    private String title;

    private LocalDate examDate;

    private ExamStatus status;

    private String partnerName;

    private String courseName;

    private String description;
}
