package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.LoginRequest;
import com.ra.base_spring_boot.dto.request.RegisterRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<APIResponse<User>> registerUser(@Valid @RequestBody RegisterRequest userRegister){
        User newUser = authService.Register(userRegister);
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(true, "Register user successfully!", newUser, HttpStatus.CREATED, LocalDateTime.now().toString()));
    }
    @PostMapping("/login")
    public ResponseEntity<APIResponse<JWTResponse>> login(@Valid @RequestBody LoginRequest userLogin){
        JWTResponse user = authService.login(userLogin);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "Login successfully!", user, HttpStatus.OK, LocalDateTime.now().toString()));
    }
}
