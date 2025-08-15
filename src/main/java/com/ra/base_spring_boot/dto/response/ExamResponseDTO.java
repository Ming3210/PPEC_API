package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponseDTO {
    private Long examId;
    private String examCode;
    private String title;
    private LocalDate examDate;
    private ExamStatus status;
    private Long courseId;
    private String courseName;
    private Long partnerId;
    private String partnerName;
}
