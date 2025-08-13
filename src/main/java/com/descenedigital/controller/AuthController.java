package com.descenedigital.controller;

import com.descenedigital.model.Role;
import com.descenedigital.model.UserAccount;
import com.descenedigital.repo.UserAccountRepo;
import com.descenedigital.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Public endpoints for user registration and login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserAccountRepo userAccountRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserAccountRepo userAccountRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userAccountRepo = userAccountRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Schema(name = "RegisterRequest", description = "User registration payload")
    public record RegisterRequest(
            @NotBlank @Schema(example = "alice") String username,
            @NotBlank @Schema(example = "S3cretP@ss") String password,
            @Schema(description = "Grant ADMIN role in addition to USER", example = "false") boolean admin
    ) {}

    @Schema(name = "AuthResponse", description = "Authentication response containing a JWT token")
    public record AuthResponse(@Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String token) {}

    @Schema(name = "LoginRequest", description = "User login payload")
    public record LoginRequest(
            @NotBlank @Schema(example = "alice") String username,
            @NotBlank @Schema(example = "S3cretP@ss") String password
    ) {}

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user and returns a JWT token for immediate use.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registered successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Username already exists",
                            content = @Content)
            }
    )
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userAccountRepo.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        UserAccount account = new UserAccount();
        account.setUsername(request.username());
        account.setPasswordHash(passwordEncoder.encode(request.password()));
        account.setRoles(request.admin() ? Set.of(Role.ADMIN, Role.USER) : Set.of(Role.USER));
        userAccountRepo.save(account);
        String token = jwtService.generateToken(account.getUsername(), Map.of("roles", account.getRoles()));
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns a JWT token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authenticated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
            }
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        String token = jwtService.generateToken(request.username(), Map.of());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
