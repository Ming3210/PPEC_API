package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.EnrollmentOnline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentOnlineRepository extends JpaRepository<EnrollmentOnline, Long> {
//    @Q
//    boolean existsByStudentIdAndCourseOffId(Long studentId, Long courseOffId);
//
//    boolean existsByStudentIdAndCourseOffIdAndIdNot(Long studentId, Long courseOffId, Long id);
//
//    long countByCourseOffId(Long courseOffId);
    int countByCourseIdIn(List<Long> courseIds);
    List<Long> findStudentIdsByCourseId(Long courseId);
}
