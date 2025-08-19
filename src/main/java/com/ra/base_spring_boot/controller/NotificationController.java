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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse<PaginationResponse<NotificationResponse>>> getAllByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return new ResponseEntity<>(
            new APIResponse<>(true, "Lấy tất cả thông báo của người dùng thành công",
                notificationService.getAllByUser(userId, page, size), HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<NotificationResponse>> update(
            @PathVariable Long id,
            @RequestBody NotificationRequest request
    ) {
        return new ResponseEntity<>(
            new APIResponse<>(true, "Cập nhật thông báo thành công",
                notificationService.update(id, request), HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<PaginationResponse<NotificationResponse>>> search(
            @RequestParam Long userId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(new APIResponse<>(
            true, "Tìm kiếm thông báo thành công", notificationService.search(userId, keyword, page, size),
                HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<APIResponse<NotificationResponse>> create(@Valid @RequestBody NotificationRequest request) {
        return new ResponseEntity<>(
            new APIResponse<>(true, "Tạo thông báo thành công",
                notificationService.create(request), HttpStatus.CREATED, LocalDateTime.now()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<APIResponse<NotificationResponse>> markAsRead(@PathVariable Long id) {
        return new ResponseEntity<>(
            new APIResponse<>(true, "Thông báo đã được đọc", notificationService.markAsRead(id),
                HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return new ResponseEntity<>(
            new APIResponse<>(true, "Xóa thông báo thành công", null, HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }
}