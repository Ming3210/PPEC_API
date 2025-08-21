package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.model.constants.ExamStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamScheduleRequest {
    @NotBlank(message = "Mã bài thi không được để trống")
    @Size(max = 50, message = "Mã bài thi không được vượt quá 50 ký tự")
    private String examCode;

    @NotBlank(message = "Tiêu đề bài thi không được để trống")
    @Size(max = 255, message = "Tiêu đề bài thi không được vượt quá 255 ký tự")
    private String title;

    @NotNull(message = "Ngày thi không được để trống")
    @Future(message = "Ngày thi không được nhỏ hơn ngày hiện tại!")
    private LocalDate examDate;

    @NotNull(message = "Trạng thái không được để trống")
    private ExamStatus status;

    @NotNull(message = "Partner ID không được để trống")
    private Long partnerId;

    private String description;
}
