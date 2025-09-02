package com.ra.base_spring_boot.dto.request;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRequestDTO {

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    private String title;

    @NotBlank(message = "Thời lượng không được để trống")
    @Pattern(regexp = "^\\d+[mMhH]$",
            message = "Thời lượng phải đúng định dạng, ví dụ: 30m hoặc 1h")
    private String duration;

    @NotNull(message = "Tổng số câu hỏi không được để trống")
    @Min(value = 1, message = "Tổng số câu hỏi phải >= 1")
    @Max(value = 500, message = "Tổng số câu hỏi tối đa là 500")
    private Integer totalQuestions;

    @NotNull(message = "Số lần làm không được để trống")
    @Min(value = 1, message = "Số lần làm phải >= 1")
    @Max(value = 10, message = "Số lần làm tối đa là 10")
    private Integer attemptsAllowed;

    @NotNull(message = "Lesson ID không được để trống")
    @Positive(message = "Lesson ID phải là số dương")
    private Long lessonId;
}
