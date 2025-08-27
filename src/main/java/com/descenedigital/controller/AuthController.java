package com.descenedigital.controller;
import com.descenedigital.repo.*;
import com.descenedigital.model.User;
import com.descenedigital.security.JwtUtils;
import com.descenedigital.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import com.descendigital.enums.Role;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final UserRepository userRepository;
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService,UserRepository userRepository) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.userService = userService;
		this.userRepository = userRepository;
	}

    
   @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody Map<String, Object> requestData) {
    System.out.println("Raw request data: " + requestData);
    
    try {
        String username = (String) requestData.get("username");
        String password = (String) requestData.get("password");
        Object roleObj = requestData.get("role");
        
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Role object: " + roleObj);
        System.out.println("Role object type: " + (roleObj != null ? roleObj.getClass() : "null"));
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                   .body(Map.of("error", "Username is required"));
        }
        
        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                   .body(Map.of("error", "Password is required"));
        }

        if (userRepository.existsByUsername(username)) {
            System.out.println("Username already exists: " + username);
            return ResponseEntity.badRequest()
                   .body(Map.of("error", "Username already exists"));
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        if (roleObj != null) {
            try {
                String roleStr = roleObj.toString();
                System.out.println("Role string: " + roleStr);
               
                if (roleStr.startsWith("ROLE_")) {
                    user.setRole(Role.valueOf(roleStr));
                } else {
                    user.setRole(Role.valueOf("ROLE_" + roleStr));
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role: " + roleObj);
                return ResponseEntity.badRequest()
                       .body(Map.of("error", "Invalid role. Use ROLE_USER or ROLE_ADMIN"));
            }
        } else {
            user.setRole(Role.ROLE_USER);
        }
        
        User registeredUser = userService.registerUser(user);
        System.out.println("User registered successfully: " + registeredUser.getUsername());
        
        return ResponseEntity.ok(Map.of(
            "message", "User registered successfully",
            "username", registeredUser.getUsername(),
            "role", registeredUser.getRole().name()
        ));
        
    } catch (Exception e) {
        System.out.println("Registration error: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Registration failed: " + e.getMessage()));
    }
}
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.get("username"),
                        loginRequest.get("password"))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken((org.springframework.security.core.userdetails.User) authentication.getPrincipal());

        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
