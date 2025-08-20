package com.ra.base_spring_boot.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizSubmissionRequest {
    private List<AnswerDTO> answers;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnswerDTO {
        private Long questionId;
        private String selectedAnswer;
    }
}

