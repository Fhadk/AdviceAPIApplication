package com.descenedigital.service;

import com.descenedigital.domain.entity.Role;
import com.descenedigital.domain.entity.User;
import com.descenedigital.domain.repo.UserRepository;
import com.descenedigital.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_success() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(false);
        when(passwordEncoder.encode("Pass@1234")).thenReturn("hash");
        when(jwtUtil.generateToken(anyLong(), anyString(), anySet())).thenReturn("token");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        String token = authService.register("a@b.com", "Pass@1234");
        assertEquals("token", token);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throws() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(true);
        assertThrows(IllegalStateException.class, () -> authService.register("a@b.com", "x"));
    }

    @Test
    void login_success() {
        User u = new User();
        u.setId(1L);
        u.setEmail("a@b.com");
        u.setPasswordHash("hash");
        u.setRoles(Set.of(Role.USER));
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("Pass@1234", "hash")).thenReturn(true);
        when(jwtUtil.generateToken(anyLong(), anyString(), anySet())).thenReturn("token");

        String token = authService.login("a@b.com", "Pass@1234");
        assertEquals("token", token);
    }

    @Test
    void login_badPassword_throws() {
        User u = new User();
        u.setId(1L);
        u.setEmail("a@b.com");
        u.setPasswordHash("hash");
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("x", "hash")).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> authService.login("a@b.com", "x"));
    }
}


