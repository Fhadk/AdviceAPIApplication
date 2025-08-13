package com.descenedigital.service;

import com.descenedigital.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.descenedigital.model.User user=userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return User.withUsername(user.getEmail()).password(user.getPassword()).roles(user.getRole().name()).build();
    }
}
