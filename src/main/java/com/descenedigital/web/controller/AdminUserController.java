package com.descenedigital.web.controller;

import com.descenedigital.domain.entity.Role;
import com.descenedigital.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userId}/roles/{role}")
    public ResponseEntity<Void> assignRole(@PathVariable Long userId, @PathVariable Role role) {
        userService.assignRole(userId, role);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/lock")
    public ResponseEntity<Void> lock(@PathVariable Long userId, @RequestParam("value") boolean value) {
        userService.setLocked(userId, value);
        return ResponseEntity.noContent().build();
    }
}


