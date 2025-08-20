package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {
    List<CourseSchedule> findByCourseOffId(Long courseOffId);
}
