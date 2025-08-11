package com.descenedigital.config;

import com.descenedigital.model.Advice;
import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AdviceRepo adviceRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        log.info("Initializing sample data...");

        // Create admin user
        User admin = User.builder()
                .username("admin")
                .email("admin@advice-api.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);

        // Create sample user
        User user = User.builder()
                .username("user")
                .email("user@advice-api.com")
                .password(passwordEncoder.encode("user123"))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        // Create sample advice
        Advice advice1 = Advice.builder()
                .message("Always be yourself, unless you can be Batman. Then always be Batman.")
                .category("Humor")
                .author(admin)
                .build();
        adviceRepo.save(advice1);

        Advice advice2 = Advice.builder()
                .message("The early bird might get the worm, but the second mouse gets the cheese.")
                .category("Life")
                .author(user)
                .build();
        adviceRepo.save(advice2);

        Advice advice3 = Advice.builder()
                .message("Don't put off until tomorrow what you can avoid altogether.")
                .category("Productivity")
                .author(admin)
                .build();
        adviceRepo.save(advice3);

        log.info("Sample data initialized successfully!");
        log.info("Admin user: username=admin, password=admin123");
        log.info("Regular user: username=user, password=user123");
    }
} 