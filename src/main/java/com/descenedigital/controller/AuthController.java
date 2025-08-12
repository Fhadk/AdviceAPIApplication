package com.descenedigital.controller;

import com.descenedigital.model.User;
import com.descenedigital.repo.UserRepository;
import com.descenedigital.security.JwtUtils;
import Enum.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    // ================== LOGIN ==================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    credentials.get("username"),
                    credentials.get("password")
                )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails);

            return ResponseEntity.ok().body(Map.of(
                "token", token,
                "username", userDetails.getUsername(),
                "roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username/password");
        }
    }

    // ================== PUBLIC REGISTER (USER ONLY) ==================
    @PostMapping("/public/register")
    public ResponseEntity<?> publicRegister(@RequestBody Map<String, String> request) {
        if (userRepository.existsByUsername(request.get("username"))) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (request.containsKey("role")) {
            return ResponseEntity.badRequest().body("Cannot specify role in public registration");
        }

        User user = new User();
        user.setUsername(request.get("username"));
        user.setPassword(passwordEncoder.encode(request.get("password")));
        user.setRole(Role.ROLE_USER); // Always USER role for public registration
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
            "message", "User registered successfully",
            "username", user.getUsername(),
            "role", user.getRole().name()
        ));
    }

    // ================== ADMIN REGISTER (CAN SET ROLE) ==================
    @PostMapping("/register")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminRegister(
        @RequestBody Map<String, String> request,
        @RequestParam(required = false) String role
    ) {
        if (userRepository.existsByUsername(request.get("username"))) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = new User();
        user.setUsername(request.get("username"));
        user.setPassword(passwordEncoder.encode(request.get("password")));

        Role finalRole = Role.ROLE_USER;
        if (role != null) {
            try {
                finalRole = Role.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid role value");
            }
        }
        user.setRole(finalRole);

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
            "message", "User registered successfully",
            "username", user.getUsername(),
            "role", user.getRole().name()
        ));
    }
}
