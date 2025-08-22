package com.ra.base_spring_boot.service.interfaces;

public interface IPasswordResetService {
    void sendResetPasswordEmail(String email);

    void resetPassword(String token, String newPassword, String confirmPassword);

    boolean isTokenValid(String token);

    void cleanupExpiredTokens();
}
