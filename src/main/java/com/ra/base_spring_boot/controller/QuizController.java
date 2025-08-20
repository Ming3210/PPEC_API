package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.QuizRequestDTO;
import com.ra.base_spring_boot.dto.request.QuizSubmissionRequest;
import com.ra.base_spring_boot.dto.response.QuizResponseDTO;
import com.ra.base_spring_boot.dto.response.QuizResultResponse;
import com.ra.base_spring_boot.service.interfaces.IQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<QuizResponseDTO> createQuiz(@RequestBody QuizRequestDTO newQuiz) {
        QuizResponseDTO createdQuiz = quizService.createNewQuiz(newQuiz);
        return ResponseEntity.ok(createdQuiz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> updateQuiz(@PathVariable Long id, @RequestBody QuizRequestDTO updateQuiz) {
        QuizResponseDTO updatedQuiz = quizService.updateNewQuiz(updateQuiz, id);
        return ResponseEntity.ok(updatedQuiz);
    }
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
    public ResponseEntity<QuizResultResponse> submitQuiz(@PathVariable Long quizId, @RequestBody QuizSubmissionRequest submission) {
        QuizResultResponse result = quizService.submitQuiz(quizId, submission);
        return ResponseEntity.ok(result);
    }
}

