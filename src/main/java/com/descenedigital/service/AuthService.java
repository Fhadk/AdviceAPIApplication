package com.descenedigital.service;

import com.descenedigital.config.JwtUtil;
import com.descenedigital.dto.auth.AuthResponse;
import com.descenedigital.dto.auth.LoginRequest;
import com.descenedigital.dto.auth.RegisterRequest;
import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;

        // Update last login
        userService.updateLastLogin(user.getUsername());

        // Generate JWT token with user roles
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", user.getRoles().stream()
                .map(Role::getValue)
                .collect(Collectors.toList()));
        extraClaims.put("userId", user.getId());

        String token = jwtUtil.generateToken(userDetails, extraClaims);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Role::getValue)
                        .collect(Collectors.toSet()))
                .message("Login successful")
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        // Create user through UserService
        userService.createUser(request);

        // Automatically login after registration
        LoginRequest loginRequest = new LoginRequest(request.getUsername(), request.getPassword());
        AuthResponse authResponse = login(loginRequest);
        authResponse.setMessage("Registration and login successful");
        
        return authResponse;
    }
}
