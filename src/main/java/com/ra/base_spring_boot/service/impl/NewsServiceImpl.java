package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.NewsRequest;
import com.ra.base_spring_boot.dto.response.NewsResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.model.News;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.repository.NewsRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;
import com.ra.base_spring_boot.service.interfaces.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ICloudinaryService cloudinaryService;

    @Override
    public NewsResponse create(NewsRequest request, Authentication authentication) {
        // Lấy user đang đăng nhập từ Authentication
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Upload ảnh lên Cloudinary
        String imageUrl = null;
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(request.getImageUrl(), "news");
        }

        News news = News.builder()
                .title(request.getTitle())
                .summary(request.getSummary())
                .content(request.getContent())
                .imageUrl(imageUrl)
                .user(user)
                .build();

        return mapToResponse(newsRepository.save(news));
    }

    @Override
    public NewsResponse update(Long id, NewsRequest request, Authentication authentication) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tin tức không tồn tại"));

        // Lấy user đang đăng nhập
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Check quyền: ADMIN hoặc chính chủ mới được update
        if (!user.getRole().equals("ADMIN") && !news.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền sửa tin này");
        }

        news.setTitle(request.getTitle());
        news.setSummary(request.getSummary());
        news.setContent(request.getContent());

        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(request.getImageUrl(), "news");
            news.setImageUrl(imageUrl);
        }

        return mapToResponse(newsRepository.save(news));
    }

    @Override
    public void delete(Long id, Authentication authentication) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tin tức không tồn tại"));

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (!user.getRole().equals("ADMIN") && !news.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền xoá tin này");
        }

        newsRepository.delete(news);
    }

    @Override
    public PaginationResponse<NewsResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.findAll(pageable);

        Page<NewsResponse> mappedPage = newsPage.map(this::mapToResponse);
        return PaginationResponse.of(mappedPage);
    }

    @Override
    public PaginationResponse<NewsResponse> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.findByTitleContainingIgnoreCase(keyword, pageable);

        Page<NewsResponse> mappedPage = newsPage.map(this::mapToResponse);
        return PaginationResponse.of(mappedPage);
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