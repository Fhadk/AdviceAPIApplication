package com.descenedigital.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestTest {

    @Test
    @DisplayName("Should create AuthRequest with builder pattern")
    void shouldCreateAuthRequestWithBuilderPattern() {
        // Given & When
        AuthRequest request = AuthRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        // Then
        assertEquals("testuser", request.getUsername());
        assertEquals("password123", request.getPassword());
    }

    @Test
    @DisplayName("Should create AuthRequest with no-args constructor")
    void shouldCreateAuthRequestWithNoArgsConstructor() {
        // Given & When
        AuthRequest request = new AuthRequest();

        // Then
        assertNotNull(request);
    }

    @Test
    @DisplayName("Should create AuthRequest with all-args constructor")
    void shouldCreateAuthRequestWithAllArgsConstructor() {
        // Given & When
        AuthRequest request = new AuthRequest("testuser", "password123");

        // Then
        assertEquals("testuser", request.getUsername());
        assertEquals("password123", request.getPassword());
    }

    @Test
    @DisplayName("Should set and get AuthRequest properties")
    void shouldSetAndGetAuthRequestProperties() {
        // Given
        AuthRequest request = new AuthRequest();

        // When
        request.setUsername("setuser");
        request.setPassword("setpassword");

        // Then
        assertEquals("setuser", request.getUsername());
        assertEquals("setpassword", request.getPassword());
    }

    @Test
    @DisplayName("Should create equal AuthRequests with same properties")
    void shouldCreateEqualAuthRequestsWithSameProperties() {
        // Given
        AuthRequest request1 = AuthRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        AuthRequest request2 = AuthRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        // When & Then
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("Should create different AuthRequests with different properties")
    void shouldCreateDifferentAuthRequestsWithDifferentProperties() {
        // Given
        AuthRequest request1 = AuthRequest.builder()
                .username("user1")
                .password("password1")
                .build();

        AuthRequest request2 = AuthRequest.builder()
                .username("user2")
                .password("password2")
                .build();

        // When & Then
        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("Should handle null username and password")
    void shouldHandleNullUsernameAndPassword() {
        // Given & When
        AuthRequest request = AuthRequest.builder()
                .username(null)
                .password(null)
                .build();

        // Then
        assertNull(request.getUsername());
        assertNull(request.getPassword());
    }

    @Test
    @DisplayName("Should handle empty username and password")
    void shouldHandleEmptyUsernameAndPassword() {
        // Given & When
        AuthRequest request = AuthRequest.builder()
                .username("")
                .password("")
                .build();

        // Then
        assertEquals("", request.getUsername());
        assertEquals("", request.getPassword());
    }

    @Test
    @DisplayName("Should create AuthRequest with special characters")
    void shouldCreateAuthRequestWithSpecialCharacters() {
        // Given & When
        AuthRequest request = AuthRequest.builder()
                .username("user@domain.com")
                .password("p@ssw0rd!@#")
                .build();

        // Then
        assertEquals("user@domain.com", request.getUsername());
        assertEquals("p@ssw0rd!@#", request.getPassword());
    }

    @Test
    @DisplayName("Should create AuthRequest with long values")
    void shouldCreateAuthRequestWithLongValues() {
        // Given
        String longUsername = "a".repeat(100);
        String longPassword = "b".repeat(100);

        // When
        AuthRequest request = AuthRequest.builder()
                .username(longUsername)
                .password(longPassword)
                .build();

        // Then
        assertEquals(longUsername, request.getUsername());
        assertEquals(longPassword, request.getPassword());
    }
}
