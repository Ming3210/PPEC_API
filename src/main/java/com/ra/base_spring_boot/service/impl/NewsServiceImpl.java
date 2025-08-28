package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.NewsRequest;
import com.ra.base_spring_boot.dto.response.NewsResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.exception.HttpUnAuthorized;
import com.ra.base_spring_boot.model.News;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.constants.NewsStatus;
import com.ra.base_spring_boot.repository.NewsRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;
import com.ra.base_spring_boot.service.interfaces.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new HttpNotFound("User không tồn tại"));

        String imageUrl = null;
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(request.getImageUrl(), "news");
        }

        // Nếu ADMIN hoặc CENTER đăng thì status = APPROVED
        NewsStatus status = (user.getRole().equals("ADMIN") || user.getRole().equals("CENTER"))
                ? NewsStatus.APPROVED
                : NewsStatus.PENDING;

        News news = News.builder()
                .title(request.getTitle())
                .summary(request.getSummary())
                .content(request.getContent())
                .imageUrl(imageUrl)
                .user(user)
                .status(status)
                .build();

        return mapToResponse(newsRepository.save(news));
    }

    @Override
    public NewsResponse update(Long id, NewsRequest request, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new HttpNotFound("User không tồn tại"));

        News news = newsRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Tin tức không tồn tại"));

        boolean isAdmin = user.getRole().equals("ADMIN");
        boolean isCenter = user.getRole().equals("CENTER");
        boolean isOwner = news.getUser().getId().equals(user.getId());

        // Chỉ cho phép ADMIN, CENTER hoặc chính chủ
        if (!(isAdmin || isCenter || isOwner)) {
            throw new HttpUnAuthorized("Bạn không có quyền sửa tin này");
        }

        // Nếu không phải ADMIN hoặc CENTER -> set lại trạng thái PENDING
        if (!isAdmin && !isCenter) {
            news.setStatus(NewsStatus.PENDING);
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
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new HttpNotFound("User không tồn tại"));

        News news = newsRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Tin tức không tồn tại"));

        boolean isAdmin = user.getRole().equals("ADMIN");
        boolean isCenter = user.getRole().equals("CENTER");
        boolean isOwner = news.getUser().getId().equals(user.getId());

        // Chỉ cho phép ADMIN, CENTER hoặc chính chủ
        if (!(isAdmin || isCenter || isOwner)) {
            throw new HttpUnAuthorized("Bạn không có quyền xoá tin này");
        }

        newsRepository.delete(news);
    }

    @Override
    public PaginationResponse<NewsResponse> getAll(int page, int size, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new HttpNotFound("User không tồn tại"));

        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage;

        if (user.getRole().equals("ADMIN") || user.getRole().equals("CENTER")) {
            newsPage = newsRepository.findAll(pageable); // thấy hết
        } else {
            newsPage = newsRepository.findByStatus(NewsStatus.APPROVED, pageable); // chỉ thấy đã duyệt
        }

        return PaginationResponse.of(newsPage.map(this::mapToResponse));
    }

    @Override
    public PaginationResponse<NewsResponse> search(String keyword, int page, int size, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new HttpNotFound("User không tồn tại"));

        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage;

        if (user.getRole().equals("ADMIN") || user.getRole().equals("CENTER")) {
            newsPage = newsRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        } else {
            newsPage = newsRepository.findByTitleContainingIgnoreCaseAndStatus(keyword, NewsStatus.APPROVED, pageable);
        }

        return PaginationResponse.of(newsPage.map(this::mapToResponse));
    }

    @Override
    public NewsResponse getDetail(Long id, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new HttpNotFound("User không tồn tại"));

        News news = newsRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Tin tức không tồn tại"));

        // ADMIN hoặc CENTER xem tất cả
        if (user.getRole().equals("ADMIN") || user.getRole().equals("CENTER")) {
            return mapToResponse(news);
        }

        // Chính chủ xem tin của mình
        if (news.getUser().getId().equals(user.getId())) {
            return mapToResponse(news);
        }

        // Người khác chỉ xem được khi đã APPROVED
        if (news.getStatus() == NewsStatus.APPROVED) {
            return mapToResponse(news);
        }

        throw new HttpUnAuthorized("Bạn không có quyền xem tin này");
    }

    @Override
    public NewsResponse approve(Long id, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new HttpNotFound("User không tồn tại"));

        // Chỉ ADMIN hoặc CENTER được duyệt tin
        if (!user.getRole().equals("ADMIN") && !user.getRole().equals("CENTER")) {
            throw new HttpUnAuthorized("Bạn không có quyền duyệt tin này");
        }

        News news = newsRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Tin tức không tồn tại"));

        news.setStatus(NewsStatus.APPROVED);
        newsRepository.save(news);

        return mapToResponse(news);
    }

    @Override
    public PaginationResponse<NewsResponse> getPendingNews(int page, int size, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new HttpNotFound("User không tồn tại"));

        if (!user.getRole().equals("ADMIN") && !user.getRole().equals("CENTER")) {
            throw new HttpUnAuthorized("Bạn không có quyền xem danh sách tin chờ duyệt");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<News> newsPage = newsRepository.findByStatus(NewsStatus.PENDING, pageable);

        return PaginationResponse.of(newsPage.map(this::mapToResponse));
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