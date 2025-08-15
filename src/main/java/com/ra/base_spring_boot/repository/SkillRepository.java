package com.ra.base_spring_boot.repository;


import com.ra.base_spring_boot.model.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    Page<Skill> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Skill> findAllByOrderByNameAsc();

    @Query("SELECT COUNT(c) FROM CourseOff c JOIN c.skills s WHERE s.id = :skillId")
    Long countCoursesUsingSkill(@Param("skillId") Long skillId);

    @Query("SELECT s FROM Skill s JOIN s.courses c WHERE c.id = :courseId")
    List<Skill> findSkillsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT s, COUNT(c) as courseCount FROM Skill s LEFT JOIN s.courses c GROUP BY s ORDER BY courseCount DESC")
    List<Object[]> findPopularSkills();
}
