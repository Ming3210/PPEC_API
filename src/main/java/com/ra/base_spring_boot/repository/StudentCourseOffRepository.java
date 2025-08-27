package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Student;
import com.ra.base_spring_boot.model.StudentCourseOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.List;

@Repository
public interface StudentCourseOffRepository extends JpaRepository<StudentCourseOff, Long> {

    Optional<StudentCourseOff> findByStudentIdAndCourseOffId(Long studentId, Long courseOffId);

    List<StudentCourseOff> findByCourseOffId(Long courseOffId);

    List<StudentCourseOff> findByStudentId(Long studentId);
    @Query("SELECT COUNT(sco) FROM StudentCourseOff sco WHERE sco.courseOff.id = ?1")
    int countByCourseIdIn(List<Long> courseIds);
    @Query("SELECT sco.student.id FROM StudentCourseOff sco WHERE sco.courseOff.id = ?1")
    List<Long> findStudentIdsByCourseId(Long courseId);

    List<StudentCourseOff> findByStudent(Student student);
}
