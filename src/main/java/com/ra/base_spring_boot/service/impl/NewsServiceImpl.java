package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.NewsRequest;
import com.ra.base_spring_boot.dto.response.NewsResponse;
import com.ra.base_spring_boot.model.News;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.repository.NewsRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public NewsResponse create(NewsRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        News news = News.builder()
                .title(request.getTitle())
                .summary(request.getSummary())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .user(user)
                .build();

        return mapToResponse(newsRepository.save(news));
    }

    @Override
    public NewsResponse update(Long id, NewsRequest request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tin tức không tồn tại"));

        news.setTitle(request.getTitle());
        news.setSummary(request.getSummary());
        news.setContent(request.getContent());
        news.setImageUrl(request.getImageUrl());

        return mapToResponse(newsRepository.save(news));
    }

    @Override
    public void delete(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public List<NewsResponse> getAll() {
        return newsRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<NewsResponse> search(String keyword) {
        return newsRepository.findByTitleContainingIgnoreCase(keyword).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private NewsResponse mapToResponse(News news) {
        return NewsResponse.builder()
                .newsId(news.getNewsId())
                .title(news.getTitle())
                .summary(news.getSummary())
                .content(news.getContent())
                .imageUrl(news.getImageUrl())
                .createdAt(news.getCreatedAt())
                .updatedAt(news.getUpdatedAt())
                .userId(news.getUser().getId())
                .fullName(news.getUser().getFullName())
                .role(news.getUser().getRole())
                .build();
    }
}