package com.descenedigital.service;

import com.descenedigital.domain.entity.Role;
import com.descenedigital.domain.entity.User;
import com.descenedigital.domain.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Assigns a role to a user.
     */
    @Transactional
    public void assignRole(Long userId, Role role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    /**
     * Locks or unlocks a user account.
     */
    @Transactional
    public void setLocked(Long userId, boolean locked) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setLocked(locked);
        userRepository.save(user);
    }
}


