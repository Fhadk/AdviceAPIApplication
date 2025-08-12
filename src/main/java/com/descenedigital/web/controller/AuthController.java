package com.descenedigital.web.controller;

import com.descenedigital.service.AuthService;
import com.descenedigital.web.dto.AuthDtos;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDtos.TokenResponseDTO> register(@Valid @RequestBody AuthDtos.RegisterRequestDTO request) {
        String token = authService.register(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthDtos.TokenResponseDTO(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.TokenResponseDTO> login(@Valid @RequestBody AuthDtos.LoginRequestDTO request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthDtos.TokenResponseDTO(token));
    }
}


