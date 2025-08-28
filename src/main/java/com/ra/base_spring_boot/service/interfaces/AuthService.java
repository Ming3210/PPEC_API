package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.LoginRequest;
import com.ra.base_spring_boot.dto.request.RegisterRequest;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.model.User;

public interface AuthService {
    User Register(RegisterRequest registerRequest);
    JWTResponse login(LoginRequest userLogin);
    boolean changeUserRole(Long userId, String newRole);
    void logout(String token);
}
