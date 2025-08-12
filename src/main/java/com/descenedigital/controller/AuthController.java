package com.descenedigital.controller;

import com.descenedigital.model.User;
import com.descenedigital.security.JwtUtil;
import com.descenedigital.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    record RegisterRequest(String username, String password) {}
    record LoginRequest(String username, String password) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        var user = userService.register(req.username(), req.password());
        return ResponseEntity.ok(Map.of("id", user.getId(), "username", user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User u = userService.findByUsername(req.username());
        if (u == null) return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        if (!passwordEncoder.matches(req.password(), u.getPassword())) return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        var roles = u.getRoles().stream().map(Enum::name).map(s -> s).toList();
        String token = jwtUtil.generateToken(u.getUsername(), u.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()));
        return ResponseEntity.ok(Map.of("token", token));
    }
}

