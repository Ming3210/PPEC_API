package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.NotificationRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.NotificationResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.service.interfaces.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Lấy danh sách thông báo của user đang đăng nhập
    @GetMapping
    public ResponseEntity<APIResponse<PaginationResponse<NotificationResponse>>> getAllByUser(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Lấy thông báo thành công",
                        notificationService.getAllByUser(authentication, page, size),
                        HttpStatus.OK, LocalDateTime.now()));
    }

    // Tạo thông báo gửi cho nhiều user
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SCHOOL_ADMIN')")
    public ResponseEntity<APIResponse<NotificationResponse>> create(
            @Valid @RequestBody NotificationRequest request
    ) {
        return new ResponseEntity<>(
                new APIResponse<>(true, "Tạo thông báo thành công",
                        notificationService.create(request),
                        HttpStatus.CREATED, LocalDateTime.now()),
                HttpStatus.CREATED);
    }

    // Cập nhật nội dung thông báo (ADMIN, SCHOOL_ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SCHOOL_ADMIN')")
    public ResponseEntity<APIResponse<NotificationResponse>> update(
            @PathVariable Long id,
            @RequestBody NotificationRequest request
    ) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Cập nhật thông báo thành công",
                        notificationService.update(id, request),
                        HttpStatus.OK, LocalDateTime.now()));
    }

    // Tìm kiếm thông báo (theo user hiện tại)
    @GetMapping("/search")
    public ResponseEntity<APIResponse<PaginationResponse<NotificationResponse>>> search(
            Authentication authentication,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Tìm kiếm thông báo thành công",
                        notificationService.search(authentication, keyword, page, size),
                        HttpStatus.OK, LocalDateTime.now()));
    }

    // Đánh dấu 1 thông báo là đã đọc (cho user hiện tại)
    @PutMapping("/{id}/read")
    public ResponseEntity<APIResponse<NotificationResponse>> markAsRead(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Thông báo đã được đọc",
                        notificationService.markAsRead(id, authentication),
                        HttpStatus.OK, LocalDateTime.now()));
    }

    // Đánh dấu 1 thông báo là chưa đọc (cho user hiện tại)
    @PutMapping("/{id}/unread")
    public ResponseEntity<APIResponse<NotificationResponse>> markAsUnread(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Đánh dấu chưa đọc thành công",
                        notificationService.markAsUnread(id, authentication),
                        HttpStatus.OK, LocalDateTime.now()));
    }

    // Đánh dấu tất cả thông báo của user hiện tại là đã đọc
    @PutMapping("/mark-all-read")
    public ResponseEntity<APIResponse<String>> markAllAsRead(Authentication authentication) {
        notificationService.markAllAsRead(authentication);
        return ResponseEntity.ok(
                new APIResponse<>(true, "Tất cả thông báo đã được đọc",
                        null, HttpStatus.OK, LocalDateTime.now()));
    }

    // Xóa thông báo
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        notificationService.delete(id, authentication);
        return ResponseEntity.ok(
                new APIResponse<>(true, "Xóa thông báo thành công",
                        null, HttpStatus.OK, LocalDateTime.now()));
    }
}
