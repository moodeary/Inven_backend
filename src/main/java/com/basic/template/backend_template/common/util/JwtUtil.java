package com.basic.template.backend_template.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;

    public JwtUtil(@Value("${jwt.secret-key:mySecretKey123456789012345678901234567890}") String secretKeyString,
                   @Value("${jwt.access-token-expiration:86400000}") long accessTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String generateAccessToken(String email, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("userId", Long.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 기존 메서드들도 유지 (하위 호환성)
    public String getEmailFromToken(String token) {
        return extractEmail(token);
    }

    public Long getUserIdFromToken(String token) {
        return extractUserId(token);
    }

    public boolean validateToken(String token) {
        return isTokenValid(token);
    }
}