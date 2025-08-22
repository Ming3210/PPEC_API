package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    @Query("SELECT qr FROM QuizResult qr " +
            "WHERE qr.quiz.id = :quizId AND qr.studentId = :studentId " +
            "ORDER BY qr.startTime DESC LIMIT 1")
    Optional<QuizResult> findLatestByQuizIdAndStudentId(
            @Param("quizId") Long quizId,
            @Param("studentId") Long studentId
    );
}

