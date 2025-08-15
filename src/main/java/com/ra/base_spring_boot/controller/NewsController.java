package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.NewsRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.NewsResponse;
import com.ra.base_spring_boot.service.interfaces.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping
    public ResponseEntity<APIResponse<NewsResponse>> create(@Valid @RequestBody NewsRequest request) {
        return new ResponseEntity<>(
                new APIResponse<>(true, "Thêm tin tức thành công", newsService.create(request),
                        HttpStatus.CREATED, LocalDateTime.now()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<NewsResponse>> update(@PathVariable Long id, @Valid @RequestBody NewsRequest request) {
        return new ResponseEntity<>(
                new APIResponse<>(true, "Cập nhật tin tức thành công", newsService.update(id, request),
                        HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> delete(@PathVariable Long id) {
        newsService.delete(id);
        return new ResponseEntity<>(
                new APIResponse<>(true, "Xóa tin tức thành công", null,
                        HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<NewsResponse>>> getAll() {
        return new ResponseEntity<>(
                new APIResponse<>(true, "Lấy tất cả tin tức thành công", newsService.getAll(),
                        HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<NewsResponse>>> search(@RequestParam String keyword) {
        return new ResponseEntity<>(
                new APIResponse<>(true, "Tìm kiếm tin tức thành công", newsService.search(keyword),
                        HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }
}
