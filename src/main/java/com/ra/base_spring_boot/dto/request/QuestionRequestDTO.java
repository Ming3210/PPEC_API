package com.ra.base_spring_boot.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequestDTO {

    @NotNull(message = "Quiz ID không được để trống")
    private Long quizId;

    @NotBlank(message = "Nội dung câu hỏi không được để trống")
    @Size(min = 5, max = 500, message = "Nội dung câu hỏi phải từ 5 đến 500 ký tự")
    private String questionText;

    @NotBlank(message = "Loại câu hỏi không được để trống")
    @Pattern(regexp = "MCQ|TRUE_FALSE|FILL_BLANK", message = "Loại câu hỏi không hợp lệ (MCQ, TRUE_FALSE, FILL_BLANK)")
    private String type;

    @NotNull(message = "Danh sách đáp án không được để trống")
    @Size(min = 2, max = 6, message = "Câu hỏi phải có từ 2 đến 6 lựa chọn")
    private List<@NotBlank(message = "Đáp án không được để trống") String> options;

    @NotBlank(message = "Đáp án đúng không được để trống")
    private String correctAnswer;

    private String explanation;

    @NotNull(message = "Thứ tự hiển thị không được để trống")
    @Min(value = 1, message = "Thứ tự hiển thị phải >= 1")
    private Integer orderNumber;
}

