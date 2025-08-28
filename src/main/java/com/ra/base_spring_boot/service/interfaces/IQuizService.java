package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.QuestionRequestDTO;
import com.ra.base_spring_boot.dto.request.QuizRequestDTO;
import com.ra.base_spring_boot.dto.request.QuizSubmissionRequest;
import com.ra.base_spring_boot.dto.response.QuestionResponse;
import com.ra.base_spring_boot.dto.response.QuestionResponseDTO;
import com.ra.base_spring_boot.dto.response.QuizResponseDTO;
import com.ra.base_spring_boot.dto.response.QuizResultResponse;

import java.util.List;

public interface IQuizService {
    List<QuizResponseDTO> listQuiz();
    QuizResponseDTO getQuizById(Long id);
    QuizResponseDTO createNewQuiz(QuizRequestDTO newQuiz);
    QuizResponseDTO updateNewQuiz(QuizRequestDTO updateQuiz, Long id);
    void deleteQuiz(Long idQuiz);
    List<QuizResponseDTO> getQuizzesByLesson(Long lessonId);
    QuizResultResponse submitQuiz(Long quizId, QuizSubmissionRequest submission);
    QuestionResponse addQuestionToQuiz(QuestionRequestDTO dto);
}

