package com.descenedigital.controller;

import com.descenedigital.dto.AuthRequest;
import com.descenedigital.dto.AuthResponse;
import com.descenedigital.model.User;
import com.descenedigital.repo.UserRepo;
import com.descenedigital.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should register new user successfully")
    void shouldRegisterNewUserSuccessfully() {
        // Given
        AuthRequest request = AuthRequest.builder()
                .username("newuser")
                .password("password123")
                .build();

        when(userRepo.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(jwtService.generateToken("newuser")).thenReturn("jwtToken");
        when(userRepo.save(any(User.class))).thenReturn(createUser("newuser", "encodedPassword", User.Role.USER));

        // When
        ResponseEntity<AuthResponse> response = authController.register(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwtToken", response.getBody().getToken());
        assertEquals("newuser", response.getBody().getUsername());
        assertEquals("User registered successfully", response.getBody().getMessage());

        verify(userRepo).existsByUsername("newuser");
        verify(passwordEncoder).encode("password123");
        verify(userRepo).save(any(User.class));
        verify(jwtService).generateToken("newuser");
    }

    @Test
    @DisplayName("Should reject registration for existing username")
    void shouldRejectRegistrationForExistingUsername() {
        // Given
        AuthRequest request = AuthRequest.builder()
                .username("existinguser")
                .password("password123")
                .build();

        when(userRepo.existsByUsername("existinguser")).thenReturn(true);

        // When
        ResponseEntity<AuthResponse> response = authController.register(request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Username already exists", response.getBody().getMessage());
        assertNull(response.getBody().getToken());

        verify(userRepo).existsByUsername("existinguser");
        verify(userRepo, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Should login user successfully with valid credentials")
    void shouldLoginUserSuccessfullyWithValidCredentials() {
        // Given
        AuthRequest request = AuthRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        User user = createUser("testuser", "encodedPassword", User.Role.USER);
        user.setEnabled(true);

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken("testuser")).thenReturn("jwtToken");

        // When
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwtToken", response.getBody().getToken());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals("Login successful", response.getBody().getMessage());

        verify(userRepo).findByUsername("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtService).generateToken("testuser");
    }

    @Test
    @DisplayName("Should reject login with invalid username")
    void shouldRejectLoginWithInvalidUsername() {
        // Given
        AuthRequest request = AuthRequest.builder()
                .username("nonexistentuser")
                .password("password123")
                .build();

        when(userRepo.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // When
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid username or password", response.getBody().getMessage());
        assertNull(response.getBody().getToken());

        verify(userRepo).findByUsername("nonexistentuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Should reject login with invalid password")
    void shouldRejectLoginWithInvalidPassword() {
        // Given
        AuthRequest request = AuthRequest.builder()
                .username("testuser")
                .password("wrongpassword")
                .build();

        User user = createUser("testuser", "encodedPassword", User.Role.USER);
        user.setEnabled(true);

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        // When
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid username or password", response.getBody().getMessage());
        assertNull(response.getBody().getToken());

        verify(userRepo).findByUsername("testuser");
        verify(passwordEncoder).matches("wrongpassword", "encodedPassword");
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Should reject login for disabled user")
    void shouldRejectLoginForDisabledUser() {
        // Given
        AuthRequest request = AuthRequest.builder()
                .username("disableduser")
                .password("password123")
                .build();

        User user = createUser("disableduser", "encodedPassword", User.Role.USER);
        user.setEnabled(false);

        when(userRepo.findByUsername("disableduser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // When
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid username or password", response.getBody().getMessage());
        assertNull(response.getBody().getToken());

        verify(userRepo).findByUsername("disableduser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Should register admin user successfully")
    void shouldRegisterAdminUserSuccessfully() {
        // Given
        AuthRequest request = AuthRequest.builder()
                .username("adminuser")
                .password("admin123")
                .build();

        when(userRepo.existsByUsername("adminuser")).thenReturn(false);
        when(passwordEncoder.encode("admin123")).thenReturn("encodedPassword");
        when(jwtService.generateToken("adminuser")).thenReturn("jwtToken");
        when(userRepo.save(any(User.class))).thenReturn(createUser("adminuser", "encodedPassword", User.Role.ADMIN));

        // When
        ResponseEntity<AuthResponse> response = authController.registerAdmin(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwtToken", response.getBody().getToken());
        assertEquals("adminuser", response.getBody().getUsername());
        assertEquals("Admin user registered successfully", response.getBody().getMessage());

        verify(userRepo).existsByUsername("adminuser");
        verify(passwordEncoder).encode("admin123");
        verify(userRepo).save(any(User.class));
        verify(jwtService).generateToken("adminuser");
    }

    @Test
    @DisplayName("Should reject admin registration for existing username")
    void shouldRejectAdminRegistrationForExistingUsername() {
        // Given
        AuthRequest request = AuthRequest.builder()
                .username("existingadmin")
                .password("admin123")
                .build();

        when(userRepo.existsByUsername("existingadmin")).thenReturn(true);

        // When
        ResponseEntity<AuthResponse> response = authController.registerAdmin(request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Username already exists", response.getBody().getMessage());
        assertNull(response.getBody().getToken());

        verify(userRepo).existsByUsername("existingadmin");
        verify(userRepo, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(anyString());
    }

    private User createUser(String username, String password, User.Role role) {
        return User.builder()
                .username(username)
                .password(password)
                .email(username + "@example.com")
                .roles(Set.of(role))
                .enabled(true)
                .build();
    }
}
