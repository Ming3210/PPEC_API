package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecturer, Long> {
    Optional<Lecturer> findByUserId(Long userId);
    @Query("SELECT l FROM Lecturer l " +
            "WHERE (:keyword IS NULL OR LOWER(l.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:specialization IS NULL OR LOWER(l.industry.name) LIKE LOWER(CONCAT('%', :specialization, '%'))) " +
            "AND (:status IS NULL OR l.isDeleted = :status)")
    List<Lecturer> searchLecturers(@Param("keyword") String keyword,
                                   @Param("specialization") String specialization,
                                   @Param("status") Boolean status);
}
