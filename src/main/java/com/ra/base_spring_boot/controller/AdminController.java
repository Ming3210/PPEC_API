package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/role")
public class AdminController {
    @Autowired
    public AuthService authService;
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Boolean>> changeRole(@PathVariable Long id, @RequestParam String role) {
        boolean updatedUser = authService.changeUserRole(id, role);
        return ResponseEntity.ok(new APIResponse<>(true, "Cập nhật role cho người dùng thành công", updatedUser, HttpStatus.OK, LocalDateTime.now()));
    }
}
