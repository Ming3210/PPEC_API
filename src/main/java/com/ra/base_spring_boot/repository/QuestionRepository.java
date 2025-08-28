package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
