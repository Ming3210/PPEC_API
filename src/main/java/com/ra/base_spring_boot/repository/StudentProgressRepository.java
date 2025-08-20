package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.StudentProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {
    List<StudentProgress> findByLessonId(Long lessonId);
    long countByLessonIdAndCompletionPercentage(Long lessonId, Double completionPercentage);
    @Query("SELECT sp FROM StudentProgress sp WHERE sp.lesson.id = :lessonId AND sp.studentId = :studentId")
    List<StudentProgress> findByLessonIdAndStudentId(Long lessonId, Long studentId);
}
