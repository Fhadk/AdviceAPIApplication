package com.descenedigital;

import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import com.descenedigital.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class AdviceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdviceApiApplication.class, args);
	}
	@Bean
	CommandLineRunner init(UserRepo repo, BCryptPasswordEncoder encoder) {
		return args -> {
			if (repo.findByEmail("admin@example.com").isEmpty()) {
				repo.save(User.builder()
						.email("admin@example.com")
						.password(encoder.encode("admin123"))
						.role(Role.ADMIN)
						.build());
			}
		};
	}
}
