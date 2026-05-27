package com.example.demosecurity.services;

public interface RefreshTokenService {

    String generateRefreshToken(String userEmail);

    String getSubjectAndMarkAsUsed(String token);

    void revokeToken(String token);

    void cleanupExpiredTokens();

}
