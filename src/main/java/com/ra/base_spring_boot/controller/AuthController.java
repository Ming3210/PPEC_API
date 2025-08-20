package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.LoginRequest;
import com.ra.base_spring_boot.dto.request.RegisterRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.security.jwt.JWTProvider;
import com.ra.base_spring_boot.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTProvider jwtProvider;
    @PostMapping("/register")
    public ResponseEntity<APIResponse<User>> registerUser(@Valid @RequestBody RegisterRequest userRegister){
        User newUser = authService.Register(userRegister);
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(true, "Register user successfully!", newUser, HttpStatus.CREATED, LocalDateTime.now()));
    }
    @PostMapping("/login")
    public ResponseEntity<APIResponse<JWTResponse>> login(@Valid @RequestBody LoginRequest userLogin){
        JWTResponse user = authService.login(userLogin);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "Login successfully!", user, HttpStatus.OK, LocalDateTime.now()));
    }
    @PostMapping("/refresh")
    public ResponseEntity<JWTResponse> refresh(@RequestParam String refreshToken) {
        if (jwtProvider.validateToken(refreshToken)) {
            String username = jwtProvider.getUsernameFromToken(refreshToken);
            String newAccessToken = jwtProvider.generateToken(username);

            return ResponseEntity.ok(
                    JWTResponse.builder()
                            .username(username)
                            .token(newAccessToken)
                            .refreshToken(refreshToken)
                            .build()
            );
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
