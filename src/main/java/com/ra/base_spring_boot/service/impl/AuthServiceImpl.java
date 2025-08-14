package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.LoginRequest;
import com.ra.base_spring_boot.dto.request.RegisterRequest;
import com.ra.base_spring_boot.dto.response.JWTResponse;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.constants.AccountStatus;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.security.jwt.JWTProvider;
import com.ra.base_spring_boot.security.principal.UserPrincipal;
import com.ra.base_spring_boot.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTProvider jwtProvider;
    @Override
    public User Register(RegisterRequest userRegister) {
        if (userRepository.findByUsername(userRegister.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Tên tài khoản đã tồn tại");
        }

        if (userRepository.findByEmail(userRegister.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        User newUser = new User();
        newUser.setUsername(userRegister.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        newUser.setFullName(userRegister.getFullName());
        newUser.setEmail(userRegister.getEmail());
        newUser.setStatus(AccountStatus.ACTIVE);
        newUser.setPhoneNumber(userRegister.getPhoneNumber());
        newUser.setRole(userRegister.getRole());
        newUser.setCreatedAt(java.time.LocalDateTime.now());
        newUser.setUpdatedAt(java.time.LocalDateTime.now());
            return userRepository.save(newUser);
    }

    @Override
    public JWTResponse login(LoginRequest userLogin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLogin.getUsernameOrEmail(),
                            userLogin.getPassword()
                    )
            );

            UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
            String token = jwtProvider.generateToken(user.getUsername());

            return JWTResponse.builder()
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .status(user.getStatus())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .authorities(user.getAuthorities())
                    .token(token)
                    .build();

        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Lỗi xác thực: " + e.getMessage());
        }
    }
}
