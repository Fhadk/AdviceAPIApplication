package com.descenedigital.service;

import com.descenedigital.domain.entity.Role;
import com.descenedigital.domain.entity.User;
import com.descenedigital.domain.repo.UserRepository;
import com.descenedigital.security.jwt.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user with USER role and returns a JWT.
     */
    @Transactional
    public String register(String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of(Role.USER));
        user = userRepository.save(user);
        return jwtUtil.generateToken(user.getId(), user.getEmail(), Set.of("USER"));
    }

    /**
     * Authenticates a user and returns a JWT.
     */
    @Transactional(readOnly = true)
    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!user.isEnabled() || user.isLocked()) {
            throw new IllegalStateException("Account disabled or locked");
        }
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        Set<String> roles = user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet());
        return jwtUtil.generateToken(user.getId(), user.getEmail(), roles);
    }
}


