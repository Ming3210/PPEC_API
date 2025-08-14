package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
