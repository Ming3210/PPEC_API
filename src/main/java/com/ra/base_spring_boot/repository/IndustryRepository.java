package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndustryRepository extends JpaRepository<Industry, Long> {
}
