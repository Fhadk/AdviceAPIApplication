package com.descenedigital.service;

import com.descenedigital.common.Messages;
import com.descenedigital.dto.JwtResponse;
import com.descenedigital.dto.LoginRequest;
import com.descenedigital.dto.RegisterRequest;
import com.descenedigital.entity.User;        // ✅ apni entity import karo
import com.descenedigital.entity.enums.Role;
import com.descenedigital.repo.UserRepo;
import com.descenedigital.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

  private final UserRepo users;
  private final PasswordEncoder encoder;
  private final JwtUtil jwtUtil;

  public AuthService(UserRepo users, PasswordEncoder encoder, JwtUtil jwtUtil) {
    this.users = users;
    this.encoder = encoder;
    this.jwtUtil = jwtUtil;
  }

  public void register(RegisterRequest req) {
    // Check if username already exists
    if (users.findByUsername(req.username()).isPresent()) {
      throw new RuntimeException("Username already taken");
    }

    // Create new user with default role USER
    User newUser = User.builder()
            .username(req.username())
            .password(encoder.encode(req.password()))
            .roles(Set.of(Role.USER))
            .enabled(true)
            .build();

    users.save(newUser);
  }

  public JwtResponse login(LoginRequest req) {
    User user = (User) users.findByUsername(req.username())
            .orElseThrow(() -> new RuntimeException(Messages.BAD_CREDENTIALS));

    if (!encoder.matches(req.password(), user.getPassword())) {
      throw new RuntimeException(Messages.BAD_CREDENTIALS);
    }

    String token = jwtUtil.generate(
            user.getUsername(),
            user.getRoles().stream().map(Enum::name).toList()
    );

    return new JwtResponse(token);
  }
}
