package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.QuizRequestDTO;
import com.ra.base_spring_boot.dto.request.QuizSubmissionRequest;
import com.ra.base_spring_boot.dto.response.QuestionResponseDTO;
import com.ra.base_spring_boot.dto.response.QuizResponseDTO;
import com.ra.base_spring_boot.dto.response.QuizResultResponse;
import com.ra.base_spring_boot.model.Lesson;
import com.ra.base_spring_boot.model.Question;
import com.ra.base_spring_boot.model.Quiz;
import com.ra.base_spring_boot.repository.LessonRepository;
import com.ra.base_spring_boot.repository.QuizRepository;
import com.ra.base_spring_boot.service.interfaces.IQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements IQuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Override
    public List<QuizResponseDTO> listQuiz() {
        return quizRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
    @Override
    public QuizResponseDTO getQuizById(Long id) {
        try {
            Quiz quiz = quizRepository.findAll().stream()
                    .filter(q -> q.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Quiz với ID: " + id));
            return mapToResponseDTO(quiz);
        } catch(Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    @Override
    public QuizResponseDTO createNewQuiz(QuizRequestDTO newQuiz) {
        Lesson lesson = lessonRepository.findById(newQuiz.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Lesson với ID: " + newQuiz.getLessonId()));

        Quiz quiz = new Quiz();
        quiz.setTitle(newQuiz.getTitle());
        quiz.setDuration(newQuiz.getDuration());
        quiz.setTotalQuestions(newQuiz.getTotalQuestions());
        quiz.setAttemptsAllowed(newQuiz.getAttemptsAllowed());
        quiz.setLesson(lesson);

        Quiz savedQuiz = quizRepository.save(quiz);

        return mapToResponseDTO(savedQuiz);
    }


    @Override
    public QuizResponseDTO updateNewQuiz(QuizRequestDTO updateQuiz, Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Quiz với ID: " + id));

        quiz.setTitle(updateQuiz.getTitle());
        quiz.setDuration(updateQuiz.getDuration());
        quiz.setTotalQuestions(updateQuiz.getTotalQuestions());
        quiz.setAttemptsAllowed(updateQuiz.getAttemptsAllowed());
        if (updateQuiz.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(updateQuiz.getLessonId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Lesson với ID: " + updateQuiz.getLessonId()));
            quiz.setLesson(lesson);
        }
        Quiz updatedQuiz = quizRepository.save(quiz);
        return mapToResponseDTO(updatedQuiz);
    }


    @Override
    public void deleteQuiz(Long idQuiz) {
        Quiz quiz = quizRepository.findById(idQuiz)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Quiz với ID: " + idQuiz));

        quizRepository.delete(quiz);
    }


    @Override
    @Transactional(readOnly = true)
    public List<QuizResponseDTO> getQuizzesByLesson(Long lessonId) {
        List<Quiz> quizzes = quizRepository.findByLessonIdWithQuestions(lessonId);
        quizzes.forEach(q -> q.getQuestions().forEach(question -> question.getOptions().size()));

        return quizzes.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }




    @Override
    @Transactional
    public QuizResultResponse submitQuiz(Long quizId, QuizSubmissionRequest submission) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy quiz với id: " + quizId));

        List<QuizSubmissionRequest.AnswerDTO> answers = submission.getAnswers();

        int totalQuestions = quiz.getQuestions() != null ? quiz.getQuestions().size() : 0;

        List<QuizResultResponse.QuestionResultDTO> details = quiz.getQuestions().stream().map(q -> {
            QuizSubmissionRequest.AnswerDTO answerDTO = answers.stream()
                    .filter(a -> a.getQuestionId().equals(q.getId()))
                    .findFirst()
                    .orElse(null);

            boolean isCorrect = answerDTO != null && q.getCorrectAnswer().equals(answerDTO.getSelectedAnswer());
            String yourAnswer = answerDTO != null ? answerDTO.getSelectedAnswer() : null;

            return QuizResultResponse.QuestionResultDTO.builder()
                    .questionId(q.getId())
                    .isCorrect(isCorrect)
                    .correctAnswer(q.getCorrectAnswer())
                    .yourAnswer(yourAnswer)
                    .build();
        }).toList();

        int correctAnswers = (int) details.stream().filter(QuizResultResponse.QuestionResultDTO::isCorrect).count();
        int wrongAnswers = totalQuestions - correctAnswers;
        int score = totalQuestions > 0 ? (int) ((double) correctAnswers / totalQuestions * 100) : 0;
        return QuizResultResponse.builder()
                .quizId(quizId)
                .score(score)
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .wrongAnswers(wrongAnswers)
                .details(details)
                .build();
    }



    private QuizResponseDTO mapToResponseDTO(Quiz quiz) {
        List<QuestionResponseDTO> questions = null;
        if (quiz.getQuestions() != null) {
            questions = quiz.getQuestions().stream().map(q -> {
                List<String> optionsCopy = null;
                if (q.getOptions() != null) {
                    optionsCopy = List.copyOf(q.getOptions());
                }
                return QuestionResponseDTO.builder()
                        .id(q.getId())
                        .questionText(q.getQuestionText())
                        .type(q.getType().name())
                        .options(optionsCopy)
                        .orderNumber(q.getOrderNumber())
                        .build();
            }).toList();
        }

        return QuizResponseDTO.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .duration(quiz.getDuration())
                .totalQuestions(quiz.getTotalQuestions())
                .attemptsAllowed(quiz.getAttemptsAllowed())
                .lessonId(quiz.getLesson() != null ? quiz.getLesson().getId() : null)
                .questions(questions)
                .build();
    }


}
