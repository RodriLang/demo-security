package com.example.demosecurity.services.impl;

import com.example.demosecurity.exceptions.InvalidTokenException;
import com.example.demosecurity.models.RefreshToken;
import com.example.demosecurity.repositories.RefreshTokenRepository;
import com.example.demosecurity.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private Duration refreshTokenExpiration;

    @Override
    public String generateRefreshToken(String userEmail) {
        // Revoca tokens existentes del usuario
        log.debug("[RefreshTokenService] Revoke existing accessToken for user={}", userEmail);
        refreshTokenRepository.revokeAllByUserEmail(userEmail);

        String token = generateSecureToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .subject(userEmail)
                .expiresAt(Instant.now().plusMillis(refreshTokenExpiration.toMillis()))
                .createdAt(Instant.now())
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        log.debug("[RefreshTokenService] Generated refresh refreshToken for user={}", userEmail);
        return refreshToken.getToken();
    }

    @Override
    public String getSubjectAndMarkAsUsed(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid or revoked refreshToken"));

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new InvalidTokenException("Refresh token has expired");
        }

        refreshToken.setUsedAt(Instant.now());
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getSubject();
    }

    public void revokeToken(String token) {
        refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .ifPresent(refreshToken -> {
                    refreshToken.setRevoked(true);
                    log.debug("[RefreshTokenRepository] Calling save to revoke refreshToken");
                    refreshTokenRepository.save(refreshToken);
                });
        log.debug("[RefreshTokenService] Revoked refresh refreshToken");
    }

    // Programamos una tarea que se ocupe de borrar de la base de datos
    // todos los tokens expirados. Correrá diariamente a las 2 AM
    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredTokens() {
        log.debug("[RefreshTokenService] Cleaning up expired tokens");
        refreshTokenRepository.deleteByExpiresAtBefore(Instant.now());
    }

    private String generateSecureToken() {
        try {
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(SecureRandom.getInstanceStrong().generateSeed(64));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not generate secure refresh token", e);
        }
    }
}
