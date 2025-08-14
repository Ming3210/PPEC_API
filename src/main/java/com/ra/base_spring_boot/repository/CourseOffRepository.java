package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.CourseOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseOffRepository extends JpaRepository<CourseOff, Long> {
    boolean existsByName(String name);
}
