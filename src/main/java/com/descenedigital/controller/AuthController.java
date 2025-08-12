package com.descenedigital.controller;

import com.descenedigital.dto.LoginRequest;
import com.descenedigital.dto.RegistrationRequest;
import com.descenedigital.model.Role;
import com.descenedigital.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and login endpoints")
public class AuthController {
    private final AuthService authService;
    @Operation(summary = "Register a new user",
            description = "Creates a new user account with email and password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or email already in use")
            })
    @PostMapping("/register")
    public String register(@Valid @RequestBody RegistrationRequest request){
        return authService.register(request);
    }
    @Operation(summary = "Login user",
            description = "Authenticate user and return JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT token returned"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            })
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
    @Operation(summary = "Change user role",
            description = "Allows ADMIN users to update roles of existing users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User role updated"),
                    @ApiResponse(responseCode = "403", description = "Access denied")
            })
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public String changeUserRole(@PathVariable Long id, @RequestParam Role role) {
        return authService.updateUserRole(id, role);
    }
}
