package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Exam;
import com.ra.base_spring_boot.until.ExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long>, JpaSpecificationExecutor<Exam> {

    boolean existsByExamCode(String examCode);
    boolean existsByExamCodeAndExamIdNot(String examCode, Long examId);

    Page<Exam> findByStatus(ExamStatus status, Pageable pageable);

    Page<Exam> findByCourseId(Long courseId, Pageable pageable);

    Page<Exam> findByPartnerId(Long partnerId, Pageable pageable);

    List<Exam> findByExamDateBetween(LocalDate startDate, LocalDate endDate);

    Page<Exam> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Exam> findByExamCodeContainingIgnoreCase(String examCode, Pageable pageable);
}
