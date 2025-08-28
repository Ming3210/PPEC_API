package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Exam;
import com.ra.base_spring_boot.model.ExamStudent;
import com.ra.base_spring_boot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ExamStudentRepository extends JpaRepository<ExamStudent, Long> {

    List<ExamStudent> findByStudent(Student student);
}
