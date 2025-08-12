package com.descenedigital;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.descenedigital.model.User;
import com.descenedigital.repo.UserRepository;

import Enum.Role;

@SpringBootApplication
public class AdviceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdviceApiApplication.class, args);
	}
	@Bean
	CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	    return args -> {
	        if (!userRepository.existsByUsername("admin")) {
	            User admin = new User();
	            admin.setUsername("admin");
	            System.out.println("Admin UserName : admin");
	            System.out.println("Admin Password : admin123");
	            admin.setPassword(passwordEncoder.encode("admin123")); // Change password!
	            admin.setRole(Role.ROLE_ADMIN);
	            userRepository.save(admin);
	            System.out.println("Admin user created!");
	        }
	    };
     }
}
