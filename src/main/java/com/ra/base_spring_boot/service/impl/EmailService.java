package com.ra.base_spring_boot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


    public void sendResetPasswordEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Đặt lại mật khẩu - Phenikaa Course Management");

        message.setText(
                "Xin chào,\n\n" +
                        "Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn tại Phenikaa Course Management.\n\n" +
                        "Để đặt lại mật khẩu, vui lòng click vào liên kết bên dưới:\n" +
                        resetLink + "\n\n" +
                        "Liên kết này sẽ hết hạn sau 1 giờ.\n\n" +
                        "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.\n\n" +
                        "Trân trọng,\n" +
                        "Đội ngũ Phenikaa Course Management"
        );

        try {
            mailSender.send(message);
            log.info("Reset password email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send reset password email to: {}", toEmail, e);
            throw new RuntimeException("Không thể gửi email. Vui lòng thử lại sau.");
        }
    }


    public void sendPasswordChangedNotification(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Mật khẩu đã được thay đổi - Phenikaa Course Management");

        message.setText(
                "Xin chào,\n\n" +
                        "Mật khẩu cho tài khoản của bạn tại Phenikaa Course Management đã được thay đổi thành công.\n\n" +
                        "Nếu bạn không thực hiện thay đổi này, vui lòng liên hệ với chúng tôi ngay lập tức.\n\n" +
                        "Trân trọng,\n" +
                        "Đội ngũ Phenikaa Course Management"
        );

        try {
            mailSender.send(message);
            log.info("Password changed notification sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password changed notification to: {}", toEmail, e);
        }
    }
}
