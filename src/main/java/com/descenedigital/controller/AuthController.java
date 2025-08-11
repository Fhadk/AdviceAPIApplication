package com.descenedigital.controller;

import com.descenedigital.dto.AuthRequest;
import com.descenedigital.dto.AuthResponse;
import com.descenedigital.model.User;
import com.descenedigital.repo.UserRepo;
import com.descenedigital.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {

    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account with USER role")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        // Check if username already exists
        if (userRepo.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(AuthResponse.builder()
                            .message("Username already exists")
                            .build());
        }

        // Create new user with USER role
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getUsername() + "@example.com") // Simple email generation
                .roles(Set.of(User.Role.USER))
                .enabled(true)
                .build();

        userRepo.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .message("User registered successfully")
                .build());
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user and returns JWT token")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return userRepo.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .filter(User::isEnabled)
                .map(user -> {
                    String token = jwtService.generateToken(user.getUsername());
                    return ResponseEntity.ok(AuthResponse.builder()
                            .token(token)
                            .username(user.getUsername())
                            .message("Login successful")
                            .build());
                })
                .orElse(ResponseEntity.badRequest()
                        .body(AuthResponse.builder()
                                .message("Invalid username or password")
                                .build()));
    }

    @PostMapping("/admin/register")
    @Operation(summary = "Register admin user", description = "Creates a new admin user account (for development)")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody AuthRequest request) {
        // Check if username already exists
        if (userRepo.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(AuthResponse.builder()
                            .message("Username already exists")
                            .build());
        }

        // Create new admin user
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getUsername() + "@admin.com")
                .roles(Set.of(User.Role.ADMIN))
                .enabled(true)
                .build();

        userRepo.save(user);

        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .message("Admin user registered successfully")
                .build());
    }
}
