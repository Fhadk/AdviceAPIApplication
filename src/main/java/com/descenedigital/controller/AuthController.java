package com.descenedigital.controller;

import com.descenedigital.security.JwtUtil;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        // For demo purposes, hardcode users
        if (username.equals("admin") && password.equals("admin123")) {
            return ResponseEntity.ok(jwtUtil.generateToken(username, "ADMIN"));
        } else if (username.equals("user") && password.equals("user123")) {
            return ResponseEntity.ok(jwtUtil.generateToken(username, "USER"));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}

