package com.descenedigital.config;

import com.descenedigital.model.Advice;
import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepository;
import com.descenedigital.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner init(UserRepository userRepository, AdviceRepository adviceRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("adminpass"));
                admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
                userRepository.save(admin);
            }
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(encoder.encode("userpass"));
                user.setRoles(Set.of(Role.ROLE_USER));
                userRepository.save(user);
            }
            if (adviceRepository.count() == 0) {
                Advice a = new Advice();
                a.setTitle("Always test your code");
                a.setContent("Write tests early and often. They save time and reduce bugs.");
                a.setAuthor("system");
                adviceRepository.save(a);
            }
        };
    }
}
