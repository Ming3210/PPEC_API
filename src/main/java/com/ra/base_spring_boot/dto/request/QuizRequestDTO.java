package com.ra.base_spring_boot.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRequestDTO {
    private String title;
    private String duration;
    private Integer totalQuestions;
    private Integer attemptsAllowed;
    private Long lessonId;
}

