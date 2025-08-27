package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.UserResponseDTO;
import com.ra.base_spring_boot.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<APIResponse<PaginationResponse<UserResponseDTO>>> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "true") boolean sortDirection,
            @RequestParam(required = false) String keyword
    ) {
        PaginationResponse<UserResponseDTO> users = userService.getAllUser(page, size, sortBy, sortDirection, keyword);
        APIResponse<PaginationResponse<UserResponseDTO>> response = new APIResponse<>(true,"Lấy danh sách user thành công", users,HttpStatus.OK, LocalDateTime.now());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        APIResponse<UserResponseDTO> response = new APIResponse<>(true,"Lấy thông tin user thành công", user,HttpStatus.OK, LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

}
