package com.ra.base_spring_boot.security.config;


import com.ra.base_spring_boot.service.interfaces.IPasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {
    @Autowired
    private IPasswordResetService passwordResetService;

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired password reset tokens...");
        passwordResetService.cleanupExpiredTokens();
        log.info("Completed cleanup of expired password reset tokens");
    }
}