package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.NewsRequest;
import com.ra.base_spring_boot.dto.response.NewsResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;

import org.springframework.security.core.Authentication;

public interface NewsService {
    NewsResponse create(NewsRequest request, Authentication authentication);
    NewsResponse update(Long id, NewsRequest request, Authentication authentication);
    void delete(Long id, Authentication authentication);
    PaginationResponse<NewsResponse> getAll(int page, int size, Authentication authentication);
    PaginationResponse<NewsResponse> search(String keyword, int page, int size, Authentication authentication);
    NewsResponse getDetail(Long id, Authentication authentication);
    NewsResponse approve(Long id, Authentication authentication);
    PaginationResponse<NewsResponse> getPendingNews(int page, int size, Authentication authentication);
}
