package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Course;
import com.ra.base_spring_boot.model.constants.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);

    Page<Course> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Course> findByLevel(Level level, Pageable pageable);
    Page<Course> findByIsActiveTrue(Pageable pageable);
    Page<Course> findByCenterId(Long centerId, Pageable pageable);

    List<Course> findByProviderContainingIgnoreCase(String provider);
    long countByCenterId(Long centerId);
}
