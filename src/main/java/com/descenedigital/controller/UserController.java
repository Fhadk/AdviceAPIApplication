package com.descenedigital.controller;

import com.descenedigital.model.User;
import com.descenedigital.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "User registration and authentication")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public User register(
            @Parameter(description = "User details", required = true)
            @Valid @RequestBody User user) {
        return userService.registerUser(user);
    }

    @Operation(summary = "Login a user and receive JWT token")
    @PostMapping("/login")
    public String login(
            @Parameter(description = "User credentials", required = true)
            @Valid @RequestBody User user) {
        return userService.verify(user);
    }
}
