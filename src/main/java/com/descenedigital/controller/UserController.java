package com.descenedigital.controller;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.descenedigital.model.User;
import com.descenedigital.repo.UserRepository;

import Enum.Role;

@RestController
@RequestMapping("/api/users")
public class UserController {
     private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<User> updateRole(
            @PathVariable Long id, 
            @RequestParam Role role) {
        return userRepository.findById(id)
            .map(user -> {
                user.setRole(role);
                return ResponseEntity.ok(userRepository.save(user));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}