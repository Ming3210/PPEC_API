package com.ra.base_spring_boot.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResultResponse {
    private Long quizId;
    private int score; // điểm %
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private List<QuestionResultDTO> details;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionResultDTO {
        private Long questionId;
        private boolean isCorrect;
        private String correctAnswer;
        private String yourAnswer;
    }
}

