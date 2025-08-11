package com.descenedigital.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    @DisplayName("Should generate valid JWT token for username")
    void shouldGenerateValidTokenForUsername() {
        // Given
        String username = "testuser";

        // When
        String token = jwtService.generateToken(username);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    @DisplayName("Should generate valid JWT token with extra claims")
    void shouldGenerateValidTokenWithExtraClaims() {
        // Given
        String username = "testuser";
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("userId", 123L);

        // When
        String token = jwtService.generateToken(extraClaims, username);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    @DisplayName("Should extract username from valid token")
    void shouldExtractUsernameFromValidToken() {
        // Given
        String username = "testuser";
        String token = jwtService.generateToken(username);

        // When
        String extractedUsername = jwtService.extractUsername(token);

        // Then
        assertEquals(username, extractedUsername);
    }

    @Test
    @DisplayName("Should validate token for correct username")
    void shouldValidateTokenForCorrectUsername() {
        // Given
        String username = "testuser";
        String token = jwtService.generateToken(username);

        // When
        boolean isValid = jwtService.isTokenValid(token, username);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject token for incorrect username")
    void shouldRejectTokenForIncorrectUsername() {
        // Given
        String username = "testuser";
        String wrongUsername = "wronguser";
        String token = jwtService.generateToken(username);

        // When
        boolean isValid = jwtService.isTokenValid(token, wrongUsername);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should extract expiration date from token")
    void shouldExtractExpirationDateFromToken() {
        // Given
        String username = "testuser";
        String token = jwtService.generateToken(username);

        // When
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("Should extract issued date from token")
    void shouldExtractIssuedDateFromToken() {
        // Given
        String username = "testuser";
        String token = jwtService.generateToken(username);

        // When
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);

        // Then
        assertNotNull(issuedAt);
        assertTrue(issuedAt.before(new Date()) || issuedAt.equals(new Date()));
    }

    @Test
    @DisplayName("Should handle null token gracefully")
    void shouldHandleNullTokenGracefully() {
        // Given
        String nullToken = null;

        // When & Then
        assertThrows(Exception.class, () -> {
            jwtService.extractUsername(nullToken);
        });
    }

    @Test
    @DisplayName("Should handle empty token gracefully")
    void shouldHandleEmptyTokenGracefully() {
        // Given
        String emptyToken = "";

        // When & Then
        assertThrows(Exception.class, () -> {
            jwtService.extractUsername(emptyToken);
        });
    }

    @Test
    @DisplayName("Should generate valid tokens for same username")
    void shouldGenerateValidTokensForSameUsername() {
        // Given
        String username = "testuser";

        // When
        String token1 = jwtService.generateToken(username);
        String token2 = jwtService.generateToken(username);

        // Then
        assertNotNull(token1);
        assertNotNull(token2);
        assertFalse(token1.isEmpty());
        assertFalse(token2.isEmpty());
        assertEquals(username, jwtService.extractUsername(token1));
        assertEquals(username, jwtService.extractUsername(token2));
        assertTrue(jwtService.isTokenValid(token1, username));
        assertTrue(jwtService.isTokenValid(token2, username));
    }
}
