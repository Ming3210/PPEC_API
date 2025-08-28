package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.PasswordResetToken;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.repository.PasswordResetTokenRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.IPasswordResetService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PasswordResetServiceImpl implements IPasswordResetService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Value("${app.reset-password.token-expiration:3600000}")
    private long tokenExpirationMs;

    @Value("${app.reset-password.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    private static final int MAX_ATTEMPTS_PER_HOUR = 3;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void sendResetPasswordEmail(String email) {
        User userOpt = userRepository.findByEmail(email).orElseThrow(()-> new HttpNotFound("Không tìm thấy người dùng với email"));


        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long recentAttempts = passwordResetTokenRepository.countByEmailAndCreatedAtAfter(email, oneHourAgo);

        if (recentAttempts >= MAX_ATTEMPTS_PER_HOUR) {
            throw new RuntimeException("Quá nhiều yêu cầu reset password. Vui lòng thử lại sau 1 giờ.");
        }

        passwordResetTokenRepository.markAllTokensAsUsedByEmail(email);

        String token = generateResetToken();
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(tokenExpirationMs / 1000);

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .email(email)
                .expiresAt(expiresAt)
                .isUsed(false)
                .build();

        passwordResetTokenRepository.save(resetToken);

        String resetLink = frontendUrl + "/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(email, resetLink);

        log.info("Reset password email sent to: {}", email);
    }

    @Override
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenAndIsUsedFalse(token)
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ hoặc đã được sử dụng"));

        if (!resetToken.isValid()) {
            throw new RuntimeException("Token đã hết hạn hoặc không hợp lệ");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setIsUsed(true);
        passwordResetTokenRepository.save(resetToken);

        log.info("Password reset successfully for email: {}", resetToken.getEmail());
    }

    @Override
    public boolean isTokenValid(String token) {
        return passwordResetTokenRepository.findByTokenAndIsUsedFalse(token)
                .map(PasswordResetToken::isValid)
                .orElse(false);
    }

    @Override
    @Transactional
    public void cleanupExpiredTokens() {
        passwordResetTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Cleaned up expired password reset tokens");
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}
