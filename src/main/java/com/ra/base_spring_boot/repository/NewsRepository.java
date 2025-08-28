package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.News;
import com.ra.base_spring_boot.model.constants.NewsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
    // Lấy tất cả tin theo status (true/false hoặc enum)
    Page<News> findByStatus(NewsStatus status, Pageable pageable);

    // Tìm kiếm theo title + status
    Page<News> findByTitleContainingIgnoreCaseAndStatus(String keyword, NewsStatus status, Pageable pageable);
    Page<News> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}