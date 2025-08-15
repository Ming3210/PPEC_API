package com.ra.base_spring_boot.dto.request;


import com.ra.base_spring_boot.model.constants.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequestDTO {

    @NotBlank(message = "Mã bài thi không được để trống")
    @Size(max = 50, message = "Mã bài thi không được vượt quá 50 ký tự")
    private String examCode;

    @NotBlank(message = "Tiêu đề bài thi không được để trống")
    @Size(max = 255, message = "Tiêu đề bài thi không được vượt quá 255 ký tự")
    private String title;

    @NotNull(message = "Ngày thi không được để trống")
    private LocalDate examDate;

    @NotNull(message = "Trạng thái không được để trống")
    private ExamStatus status;

    @NotNull(message = "Course ID không được để trống")
    private Long courseId;

    @NotNull(message = "Partner ID không được để trống")
    private Long partnerId;
}
