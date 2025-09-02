package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.QuestionRequestDTO;
import com.ra.base_spring_boot.dto.request.QuizRequestDTO;
import com.ra.base_spring_boot.dto.request.QuizSubmissionRequest;
import com.ra.base_spring_boot.dto.response.QuestionResponse;
import com.ra.base_spring_boot.dto.response.QuizResponseDTO;
import com.ra.base_spring_boot.dto.response.QuizResultResponse;
import com.ra.base_spring_boot.service.interfaces.IQuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private IQuizService quizService;

    @GetMapping
    public ResponseEntity<List<QuizResponseDTO>> getAllQuizzes() {
        List<QuizResponseDTO> quizzes = quizService.listQuiz();
        return ResponseEntity.ok(quizzes);
    }
    @GetMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> getQuizById(@PathVariable Long id) {
        QuizResponseDTO quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SCHOOL_ADMIN', 'ROLE_SERVICE_STAFF', 'ROLE_STAFF', 'ROLE_CENTER', 'ROLE_LECTURER', 'ROLE_ASSISTANT')")
    @PostMapping
    public ResponseEntity<QuizResponseDTO> createQuiz(@Valid @RequestBody QuizRequestDTO newQuiz) {
        QuizResponseDTO createdQuiz = quizService.createNewQuiz(newQuiz);
        return ResponseEntity.ok(createdQuiz);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SCHOOL_ADMIN', 'ROLE_SERVICE_STAFF', 'ROLE_STAFF', 'ROLE_CENTER', 'ROLE_LECTURER', 'ROLE_ASSISTANT')")
    @PutMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> updateQuiz(@PathVariable Long id, @Valid @RequestBody QuizRequestDTO updateQuiz) {
        QuizResponseDTO updatedQuiz = quizService.updateNewQuiz(updateQuiz, id);
        return ResponseEntity.ok(updatedQuiz);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SCHOOL_ADMIN', 'ROLE_SERVICE_STAFF', 'ROLE_STAFF', 'ROLE_CENTER', 'ROLE_LECTURER', 'ROLE_ASSISTANT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<QuizResponseDTO>> getQuizzesByLesson(@PathVariable Long lessonId) {
        List<QuizResponseDTO> quizzes = quizService.getQuizzesByLesson(lessonId);
        return ResponseEntity.ok(quizzes);
    }

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<QuizResultResponse> submitQuiz(@PathVariable Long quizId,@Valid @RequestBody QuizSubmissionRequest submission) {
        QuizResultResponse result = quizService.submitQuiz(quizId, submission);
        return ResponseEntity.ok(result);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SCHOOL_ADMIN', 'ROLE_SERVICE_STAFF', 'ROLE_STAFF', 'ROLE_CENTER', 'ROLE_LECTURER', 'ROLE_ASSISTANT')")
    @PostMapping("/{quizId}/questions")
    public ResponseEntity<QuestionResponse> addQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionRequestDTO dto) {
        dto.setQuizId(quizId);
        return ResponseEntity.ok(quizService.addQuestionToQuiz(dto));
    }
}

