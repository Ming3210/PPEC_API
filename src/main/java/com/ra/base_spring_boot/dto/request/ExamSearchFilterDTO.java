package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.until.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSearchFilterDTO {
    private String examCode;
    private String title;
    private ExamStatus status;
    private Long courseId;
    private Long partnerId;
    private LocalDate examDateFrom;
    private LocalDate examDateTo;
    private String sortBy = "examDate";
    private String sortDirection = "desc";
    private int page = 0;
    private int size = 10;
}
