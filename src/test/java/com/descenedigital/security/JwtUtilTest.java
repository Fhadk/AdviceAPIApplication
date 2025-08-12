package com.descenedigital.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails testUser;

    @BeforeEach
    void setUp() {
        // Use a test secret key
        String testSecret = "dGhpc0lzQVRlc3RTZWNyZXRLZXlGb3JKV1RUZXN0aW5nVGhhdElzTG9uZ0Vub3VnaEZvckhNQUNTSEEyNTY=";
        jwtUtil = new JwtUtil(testSecret, 3600000); // 1 hour expiration
        
        testUser = User.builder()
                .username("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(testUser);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // JWT should have 3 parts separated by dots
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT should have 3 parts: header.payload.signature");
        
        // Each part should not be empty
        assertTrue(parts[0].length() > 0, "Header should not be empty");
        assertTrue(parts[1].length() > 0, "Payload should not be empty");
        assertTrue(parts[2].length() > 0, "Signature should not be empty");
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken(testUser);
        String extractedUsername = jwtUtil.extractUsername(token);
        
        assertEquals("testuser", extractedUsername);
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken(testUser);
        
        assertTrue(jwtUtil.validateToken(token, testUser));
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testValidateTokenWithWrongUser() {
        String token = jwtUtil.generateToken(testUser);
        
        UserDetails wrongUser = User.builder()
                .username("wronguser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
        
        assertFalse(jwtUtil.validateToken(token, wrongUser));
    }

    @Test
    void testValidateInvalidToken() {
        String invalidToken = "invalid.token.here";
        
        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void testTokenExpiration() {
        // Create JWT util with very short expiration for testing
        JwtUtil shortExpirationJwtUtil = new JwtUtil(
            "dGhpc0lzQVRlc3RTZWNyZXRLZXlGb3JKV1RUZXN0aW5nVGhhdElzTG9uZ0Vub3VnaEZvckhNQUNTSEEyNTY=", 
            100
        );
        
        String token = shortExpirationJwtUtil.generateToken(testUser);
        
        // Wait for token to expire
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }
        
        // Token should be expired and validation should fail
        assertFalse(shortExpirationJwtUtil.validateToken(token, testUser));
        assertFalse(shortExpirationJwtUtil.validateToken(token));
    }
} 