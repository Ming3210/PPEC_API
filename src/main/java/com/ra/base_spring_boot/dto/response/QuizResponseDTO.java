package com.ra.base_spring_boot.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResponseDTO {
    private Long id;
    private String title;
    private String duration;
    private Integer totalQuestions;
    private Integer attemptsAllowed;
    private Long lessonId;
    private List<QuestionResponseDTO> questions;
}

