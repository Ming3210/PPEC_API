package com.ra.base_spring_boot.repository;


import com.ra.base_spring_boot.model.CourseOff;
import com.ra.base_spring_boot.model.constants.TargetAudience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CourseOffRepository extends JpaRepository<CourseOff, Long>, JpaSpecificationExecutor<CourseOff> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    Page<CourseOff> findByTargetAudience(TargetAudience targetAudience, Pageable pageable);

    Page<CourseOff> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT c FROM CourseOff c WHERE c.price BETWEEN :from AND :to")
    List<CourseOff> findByPriceRange(@Param("from") BigDecimal from, @Param("to") BigDecimal to);

    @Query("SELECT c FROM CourseOff c WHERE c.estimatedHours BETWEEN :from AND :to")
    List<CourseOff> findByEstimatedHoursRange(@Param("from") Integer from, @Param("to") Integer to);

    @Query("SELECT c FROM CourseOff c JOIN c.skills s WHERE s.id = :skillId")
    Page<CourseOff> findBySkillId(@Param("skillId") Long skillId, Pageable pageable);
    @Query("SELECT c FROM CourseOff c WHERE c.partnerId = :partnerId")
    List<CourseOff> findByPartnerId(@Param("partnerId") Long partnerId);
}
