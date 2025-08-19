package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.StudentCourseOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface StudentCourseOffRepository extends JpaRepository<StudentCourseOff, Long> {

    Optional<StudentCourseOff> findByStudentIdAndCourseOffId(Long studentId, Long courseOffId);

    List<StudentCourseOff> findByCourseOffId(Long courseOffId);

    List<StudentCourseOff> findByStudentId(Long studentId);
}
