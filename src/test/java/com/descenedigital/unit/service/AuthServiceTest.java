package com.descenedigital.unit.service;

import com.descenedigital.dto.RegistrationRequest;
import com.descenedigital.model.User;
import com.descenedigital.repo.UserRepo;
import com.descenedigital.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepo, null, null);
        try {
            var field = AuthService.class.getDeclaredField("passwordEncoder");
            field.setAccessible(true);
            field.set(authService, passwordEncoder);
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void register_Success() {
        var request = new RegistrationRequest("newuser@example.com", "pass1234");

        when(userRepo.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = authService.register(request);

        assertEquals("User registered successfully", result);

        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void register_EmailAlreadyExists_Throws() {
        var request = new RegistrationRequest("existing@example.com", "pass1234");

        when(userRepo.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });

        assertEquals("Email already in use", exception.getMessage());

        verify(userRepo, never()).save(any());
    }
}
