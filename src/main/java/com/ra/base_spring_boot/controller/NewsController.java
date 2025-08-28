package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.NewsRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.NewsResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.service.interfaces.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse<NewsResponse>> create(
            @Valid @ModelAttribute NewsRequest request,
            Authentication authentication
    ) {
        return new ResponseEntity<>(
                new APIResponse<>(
                        true,
                        "Thêm tin tức thành công",
                        newsService.create(request, authentication),
                        HttpStatus.CREATED,
                        LocalDateTime.now()
                ),
                HttpStatus.CREATED
        );
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse<NewsResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute NewsRequest request,
            Authentication authentication
    ) {
        return new ResponseEntity<>(
                new APIResponse<>(
                        true,
                        "Cập nhật tin tức thành công",
                        newsService.update(id, request, authentication),
                        HttpStatus.OK,
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        newsService.delete(id, authentication);
        return new ResponseEntity<>(
                new APIResponse<>(
                        true,
                        "Xóa tin tức thành công",
                        null,
                        HttpStatus.OK,
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<APIResponse<PaginationResponse<NewsResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
           Authentication authentication
    ) {
        return new ResponseEntity<>(
                new APIResponse<>(true, "Lấy tất cả tin tức thành công",
                        newsService.getAll(page, size, authentication), HttpStatus.OK, LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<PaginationResponse<NewsResponse>>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        return new ResponseEntity<>(
                new APIResponse<>(true, "Tìm kiếm tin tức thành công",
                        newsService.search(keyword, page, size, authentication), HttpStatus.OK, LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<NewsResponse>> getDetail(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return new ResponseEntity<>(
                new APIResponse<>(
                        true,
                        "Xem chi tiết tin tức thành công",
                        newsService.getDetail(id, authentication),
                        HttpStatus.OK,
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<APIResponse<PaginationResponse<NewsResponse>>> getPendingNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        return new ResponseEntity<>(
                new APIResponse<>(
                        true,
                        "Lấy danh sách tin chờ duyệt thành công",
                        newsService.getPendingNews(page, size, authentication),
                        HttpStatus.OK,
                        LocalDateTime.now()
                ),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<APIResponse<NewsResponse>> approveNews(
            @PathVariable Long id,
            Authentication authentication
    ) {
        NewsResponse response = newsService.approve(id, authentication);
        return new ResponseEntity<>(
            new APIResponse<>(true, "Duyệt tin thành công", response, HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }
}
