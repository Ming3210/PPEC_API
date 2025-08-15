package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.NewsRequest;
import com.ra.base_spring_boot.dto.response.NewsResponse;

import java.util.List;

public interface NewsService {
    NewsResponse create(NewsRequest request);
    NewsResponse update(Long id, NewsRequest request);
    void delete(Long id);
    List<NewsResponse> getAll();
    List<NewsResponse> search(String keyword);
}