package com.brainwave.atmapp.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final String jwtSecret;
    private final Long jwtExpiration;
    private final SecretKey secretKey;

    public JwtUtil(Dotenv dotenv) {
        jwtSecret = dotenv.get("JWT_SECRET");
        jwtExpiration = Long.parseLong(dotenv.get("JWT_EXPIRATION"));
        secretKey = initializeSecretKey();
    }


    private SecretKey initializeSecretKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 256 bits");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getSecretKey() {
        return secretKey;
    }

    public String generateToken(String cardNumber) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(cardNumber)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }

    public String extractCardNumber(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}