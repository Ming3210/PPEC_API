package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.ForgotPasswordRequest;
import com.ra.base_spring_boot.dto.request.LoginRequest;
import com.ra.base_spring_boot.dto.request.RegisterRequest;
import com.ra.base_spring_boot.dto.request.ResetPasswordRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.security.jwt.JWTProvider;
import com.ra.base_spring_boot.service.impl.PasswordResetServiceImpl;
import com.ra.base_spring_boot.service.interfaces.AuthService;
import com.ra.base_spring_boot.service.interfaces.IPasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private IPasswordResetService passwordResetService;
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok("Đăng xuất thành công");
    }
    @PostMapping("/forgot-password")
    @Operation(summary = "Gửi email đặt lại mật khẩu")
    public ResponseEntity<APIResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
            passwordResetService.sendResetPasswordEmail(request.getEmail());
            APIResponse<String> response = APIResponse.<String>builder()
                    .status(true)
                    .message("Nếu email tồn tại trong hệ thống, chúng tôi đã gửi hướng dẫn đặt lại mật khẩu.")
                    .data("Email đã được gửi")
                    .httpStatus(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);


    }

    @PostMapping("/reset-password")
    @Operation(summary = "Đặt lại mật khẩu bằng token")
    public ResponseEntity<APIResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(
                    request.getToken(),
                    request.getNewPassword(),
                    request.getConfirmPassword()
            );

            APIResponse<String> response = APIResponse.<String>builder()
                    .status(true)
                    .message("Mật khẩu đã được đặt lại thành công.")
                    .data("Password reset successful")
                    .httpStatus(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            APIResponse<String> response = APIResponse.<String>builder()
                    .status(false)
                    .message(e.getMessage())
                    .data(null)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/validate-reset-token")
    @Operation(summary = "Kiểm tra tính hợp lệ của token")
    public ResponseEntity<APIResponse<Boolean>> validateResetToken(@RequestParam String token) {
        boolean isValid = passwordResetService.isTokenValid(token);

        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(true)
                .message(isValid ? "Token hợp lệ" : "Token không hợp lệ hoặc đã hết hạn")
                .data(isValid)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
