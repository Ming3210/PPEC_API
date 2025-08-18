package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.NewsRequest;
import com.ra.base_spring_boot.dto.response.NewsResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;

public interface NewsService {
    NewsResponse create(NewsRequest request);
    NewsResponse update(Long id, NewsRequest request);
    void delete(Long id);
    PaginationResponse<NewsResponse> getAll(int page, int size);
    PaginationResponse<NewsResponse> search(String keyword, int page, int size);
}
