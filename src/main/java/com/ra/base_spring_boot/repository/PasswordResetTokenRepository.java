package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenAndIsUsedFalse(String token);

    Optional<PasswordResetToken> findByEmailAndIsUsedFalse(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken p WHERE p.expiresAt < :now")
    void deleteExpiredTokens(LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE PasswordResetToken p SET p.isUsed = true WHERE p.email = :email")
    void markAllTokensAsUsedByEmail(String email);

    long countByEmailAndCreatedAtAfter(String email, LocalDateTime after);
}