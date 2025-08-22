package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    @Query("SELECT DISTINCT q FROM Quiz q LEFT JOIN FETCH q.questions qu WHERE q.lesson.id = :lessonId")
    List<Quiz> findByLessonIdWithQuestions(@Param("lessonId") Long lessonId);
}
