package com.descenedigital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.descenedigital.domain.entity.Role;
import com.descenedigital.domain.entity.User;
import com.descenedigital.domain.repo.UserRepository;
import org.springframework.boot.ApplicationRunner;

@SpringBootApplication
public class AdviceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdviceApiApplication.class, args);
	}

    @Bean
    ApplicationRunner seedAdmin(UserRepository users, PasswordEncoder encoder, org.springframework.core.env.Environment env) {
        return args -> {
            String email = env.getProperty("app.admin.email", "admin@example.com");
            String password = env.getProperty("app.admin.password", "Admin@1234");
            users.findByEmail(email).orElseGet(() -> {
                User admin = new User();
                admin.setEmail(email);
                admin.setPasswordHash(encoder.encode(password));
                admin.setRoles(java.util.Set.of(Role.ADMIN, Role.USER));
                return users.save(admin);
            });
        };
    }
}
