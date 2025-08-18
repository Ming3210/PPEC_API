package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
    Page<News> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}