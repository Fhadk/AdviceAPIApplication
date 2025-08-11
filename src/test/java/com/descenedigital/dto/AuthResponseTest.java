package com.descenedigital.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    @DisplayName("Should create AuthResponse with builder pattern")
    void shouldCreateAuthResponseWithBuilderPattern() {
        // Given & When
        AuthResponse response = AuthResponse.builder()
                .token("jwtToken123")
                .username("testuser")
                .message("Login successful")
                .build();

        // Then
        assertEquals("jwtToken123", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    @DisplayName("Should create AuthResponse with no-args constructor")
    void shouldCreateAuthResponseWithNoArgsConstructor() {
        // Given & When
        AuthResponse response = new AuthResponse();

        // Then
        assertNotNull(response);
    }

    @Test
    @DisplayName("Should create AuthResponse with all-args constructor")
    void shouldCreateAuthResponseWithAllArgsConstructor() {
        // Given & When
        AuthResponse response = new AuthResponse("jwtToken123", "testuser", "Login successful");

        // Then
        assertEquals("jwtToken123", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    @DisplayName("Should set and get AuthResponse properties")
    void shouldSetAndGetAuthResponseProperties() {
        // Given
        AuthResponse response = new AuthResponse();

        // When
        response.setToken("setToken");
        response.setUsername("setuser");
        response.setMessage("set message");

        // Then
        assertEquals("setToken", response.getToken());
        assertEquals("setuser", response.getUsername());
        assertEquals("set message", response.getMessage());
    }

    @Test
    @DisplayName("Should create equal AuthResponses with same properties")
    void shouldCreateEqualAuthResponsesWithSameProperties() {
        // Given
        AuthResponse response1 = AuthResponse.builder()
                .token("jwtToken123")
                .username("testuser")
                .message("Login successful")
                .build();

        AuthResponse response2 = AuthResponse.builder()
                .token("jwtToken123")
                .username("testuser")
                .message("Login successful")
                .build();

        // When & Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("Should create different AuthResponses with different properties")
    void shouldCreateDifferentAuthResponsesWithDifferentProperties() {
        // Given
        AuthResponse response1 = AuthResponse.builder()
                .token("token1")
                .username("user1")
                .message("message1")
                .build();

        AuthResponse response2 = AuthResponse.builder()
                .token("token2")
                .username("user2")
                .message("message2")
                .build();

        // When & Then
        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("Should handle null token, username and message")
    void shouldHandleNullTokenUsernameAndMessage() {
        // Given & When
        AuthResponse response = AuthResponse.builder()
                .token(null)
                .username(null)
                .message(null)
                .build();

        // Then
        assertNull(response.getToken());
        assertNull(response.getUsername());
        assertNull(response.getMessage());
    }

    @Test
    @DisplayName("Should handle empty token, username and message")
    void shouldHandleEmptyTokenUsernameAndMessage() {
        // Given & When
        AuthResponse response = AuthResponse.builder()
                .token("")
                .username("")
                .message("")
                .build();

        // Then
        assertEquals("", response.getToken());
        assertEquals("", response.getUsername());
        assertEquals("", response.getMessage());
    }

    @Test
    @DisplayName("Should create successful login response")
    void shouldCreateSuccessfulLoginResponse() {
        // Given & When
        AuthResponse response = AuthResponse.builder()
                .token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTYzNDU2Nzg5MCwiZXhwIjoxNjM0NjU0MjkwfQ.signature")
                .username("testuser")
                .message("Login successful")
                .build();

        // Then
        assertTrue(response.getToken().startsWith("eyJ"));
        assertEquals("testuser", response.getUsername());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    @DisplayName("Should create registration response")
    void shouldCreateRegistrationResponse() {
        // Given & When
        AuthResponse response = AuthResponse.builder()
                .token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXd1c2VyIiwiaWF0IjoxNjM0NTY3ODkwLCJleHAiOjE2MzQ2NTQyOTB9.signature")
                .username("newuser")
                .message("User registered successfully")
                .build();

        // Then
        assertTrue(response.getToken().startsWith("eyJ"));
        assertEquals("newuser", response.getUsername());
        assertEquals("User registered successfully", response.getMessage());
    }

    @Test
    @DisplayName("Should create error response")
    void shouldCreateErrorResponse() {
        // Given & When
        AuthResponse response = AuthResponse.builder()
                .token(null)
                .username(null)
                .message("Invalid username or password")
                .build();

        // Then
        assertNull(response.getToken());
        assertNull(response.getUsername());
        assertEquals("Invalid username or password", response.getMessage());
    }

    @Test
    @DisplayName("Should create admin registration response")
    void shouldCreateAdminRegistrationResponse() {
        // Given & When
        AuthResponse response = AuthResponse.builder()
                .token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYzNDU2Nzg5MCwiZXhwIjoxNjM0NjU0MjkwfQ.signature")
                .username("admin")
                .message("Admin user registered successfully")
                .build();

        // Then
        assertTrue(response.getToken().startsWith("eyJ"));
        assertEquals("admin", response.getUsername());
        assertEquals("Admin user registered successfully", response.getMessage());
    }

    @Test
    @DisplayName("Should create response with long token")
    void shouldCreateResponseWithLongToken() {
        // Given
        String longToken = "a".repeat(500);

        // When
        AuthResponse response = AuthResponse.builder()
                .token(longToken)
                .username("testuser")
                .message("Test message")
                .build();

        // Then
        assertEquals(longToken, response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("Test message", response.getMessage());
    }

    @Test
    @DisplayName("Should create response with special characters in message")
    void shouldCreateResponseWithSpecialCharactersInMessage() {
        // Given & When
        AuthResponse response = AuthResponse.builder()
                .token("jwtToken")
                .username("testuser")
                .message("Login successful! @#$%^&*()")
                .build();

        // Then
        assertEquals("jwtToken", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("Login successful! @#$%^&*()", response.getMessage());
    }
}
