package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.TeachingAssistant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingAssistantRepository extends JpaRepository<TeachingAssistant,Long> {
    @Query("SELECT COUNT(ta) > 0 FROM TeachingAssistant ta WHERE ta.taCode = ?1")
    boolean existsByEmployeeCode(String employeeCode);
}
