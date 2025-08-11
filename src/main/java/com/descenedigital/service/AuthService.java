package com.descenedigital.service;

import com.descenedigital.dto.AuthRequest;
import com.descenedigital.mapper.UserMapper;
import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import com.descenedigital.repo.UserRepo;
import com.descenedigital.security.JwtUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    public AuthService(UserRepo userRepo,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authManager,
                       JwtUtils jwtUtils,
                       UserMapper userMapper) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    public void register(AuthRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("username already taken");
        }
        User u = userMapper.toEntity(req);
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u.setRoles(Set.of(Role.ROLE_USER));
        userRepo.save(u);
    }

    public String login(AuthRequest req) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        var principal = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        Set<String> roles = principal.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet());
        return jwtUtils.generateToken(principal.getUsername(), roles);
    }
}
