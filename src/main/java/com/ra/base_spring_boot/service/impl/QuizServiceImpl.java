package com.ra.base_spring_boot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra.base_spring_boot.dto.request.QuestionRequestDTO;
import com.ra.base_spring_boot.dto.request.QuizRequestDTO;
import com.ra.base_spring_boot.dto.request.QuizSubmissionRequest;
import com.ra.base_spring_boot.dto.response.QuestionResponse;
import com.ra.base_spring_boot.dto.response.QuestionResponseDTO;
import com.ra.base_spring_boot.dto.response.QuizResponseDTO;
import com.ra.base_spring_boot.dto.response.QuizResultResponse;
import com.ra.base_spring_boot.model.*;
import com.ra.base_spring_boot.model.constants.QuestionType;
import com.ra.base_spring_boot.model.constants.SubmissionStatus;
import com.ra.base_spring_boot.repository.*;
import com.ra.base_spring_boot.security.principal.UserPrincipal;
import com.ra.base_spring_boot.service.interfaces.IQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.desktop.UserSessionEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements IQuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuizResultRepository quizResultRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    @Transactional
    public List<QuizResponseDTO> listQuiz() {
        return quizRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
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
    @Transactional
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
    public QuizResponseDTO getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Quiz với ID: " + id));

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user với id " + userPrincipal.getId()));

        QuizResult quizResult = QuizResult.builder()
                .quiz(quiz)
                .studentId(user.getId())
                .totalQuestions(quiz.getTotalQuestions())
                .startTime(LocalDateTime.now())
                .status(SubmissionStatus.STARTED)
                .build();

        quizResultRepository.save(quizResult);

        return mapToResponseDTO(quiz);
    }

    @Override
    @Transactional
    public QuizResultResponse submitQuiz(Long quizId, QuizSubmissionRequest submission) {
        Quiz quiz = quizRepository.findAll().stream().filter(q->q.getId().equals(quizId)).findFirst().orElseThrow(()-> new IllegalArgumentException("Không tìm thấy bài quiz với id " + quizId));

        List<QuizSubmissionRequest.AnswerDTO> answers = submission.getAnswers();
        int totalQuestions = quiz.getQuestions() != null ? quiz.getQuestions().size() : 0;

        List<QuizResultResponse.QuestionResultDTO> details = quiz.getQuestions().stream().map(q -> {
            QuizSubmissionRequest.AnswerDTO answerDTO = answers.stream()
                    .filter(a -> a.getQuestionId().equals(q.getId()))
                    .findFirst()
                    .orElseThrow(()-> new IllegalArgumentException("Không tìm thấy câu hỏi với id " + q.getId()));

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
        int score = totalQuestions > 0 ? (int) ((double) correctAnswers / totalQuestions * 100) : 0;

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user với id " + userPrincipal.getId()));

        QuizResult quizResult = quizResultRepository
                .findLatestByQuizIdAndStudentId(quizId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kết quả quiz để nộp"));

        LocalDateTime now = LocalDateTime.now();
        int timeSpent = (int) java.time.Duration.between(quizResult.getStartTime(), now).toSeconds();

        quizResult.setCorrectAnswers(correctAnswers);
        quizResult.setScore(score);
        quizResult.setTimeSpent(timeSpent);
        quizResult.setStatus(SubmissionStatus.COMPLETED);
        quizResult.setSubmittedAt(now);

        quizResultRepository.save(quizResult);

        return QuizResultResponse.builder()
                .quizId(quizId)
                .score(score)
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .wrongAnswers(totalQuestions - correctAnswers)
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
    @Override
    @Transactional
    public QuestionResponse addQuestionToQuiz(QuestionRequestDTO dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy quiz với id: " + dto.getQuizId()));
        Question question = new Question();
        question.setQuiz(quiz);
        question.setQuestionText(dto.getQuestionText());
        question.setType(QuestionType.valueOf(dto.getType()));
        question.setOptions(dto.getOptions() != null ? Set.copyOf(dto.getOptions()) : null);
        question.setCorrectAnswer(dto.getCorrectAnswer());
        question.setExplanation(dto.getExplanation());
        question.setOrderNumber(dto.getOrderNumber());
        question.setCreatedAt(LocalDateTime.now());

        Question saved = questionRepository.save(question);
        return QuestionResponse.builder()
                .id(saved.getId())
                .questionText(saved.getQuestionText())
                .type(saved.getType().name())
                .options(saved.getOptions() != null ? List.copyOf(saved.getOptions()) : null)
                .correctAnswer(saved.getCorrectAnswer())
                .explanation(saved.getExplanation())
                .orderNumber(saved.getOrderNumber())
                .build();
    }



}
