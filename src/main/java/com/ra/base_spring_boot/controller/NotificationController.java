package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.NotificationRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.NotificationResponse;
import com.ra.base_spring_boot.service.interfaces.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse<List<NotificationResponse>>> getAllByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(
            new APIResponse<>(true, "Lấy tất cả thông báo của người dùng thành công",
                notificationService.getAllByUser(userId), HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
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