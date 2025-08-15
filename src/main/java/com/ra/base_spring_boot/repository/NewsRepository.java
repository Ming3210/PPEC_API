package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByTitleContainingIgnoreCase(String keyword);
}