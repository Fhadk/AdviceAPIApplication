package com.descenedigital.service;

import com.descenedigital.dto.LoginRequest;
import com.descenedigital.dto.RegistrationRequest;
import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import com.descenedigital.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String register(RegistrationRequest request) {
        if(userRepo.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already in use");
        }
        User user = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();
        userRepo.save(user);
        return "User registered successfully";
    }
    public String login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
    return jwtService.generateToken(request.getEmail());
    }
    public String updateUserRole(Long userId, Role newRole) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(newRole);
        userRepo.save(user);
        return "Role updated to " + newRole;
    }
}
