package com.descenedigital.config;

import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.RatingRepository;
import com.descenedigital.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AdviceRepo adviceRepo;
    private final RatingRepository ratingRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Loading sample data...");
            loadSampleData();
            log.info("Sample data loaded successfully!");
        } else {
            log.info("Sample data already exists, skipping data loading.");
        }
    }

    private void loadSampleData() {
        // Create users
        User admin = createUser("admin", "admin@adviceapi.com", "password", "Admin", "User", Set.of(Role.ADMIN, Role.USER));
        User john = createUser("john_doe", "john@example.com", "password", "John", "Doe", Set.of(Role.USER));
        User jane = createUser("jane_smith", "jane@example.com", "password", "Jane", "Smith", Set.of(Role.USER));
        User mike = createUser("mike_wilson", "mike@example.com", "password", "Mike", "Wilson", Set.of(Role.USER));

        // Create advice
        Advice advice1 = createAdvice("Start Your Day Early", 
            "Wake up early to have more productive hours in your day. The morning hours are often the most peaceful and allow for better focus.",
            "A simple yet effective productivity tip that has helped many successful people achieve their goals.", john);

        Advice advice2 = createAdvice("Practice Gratitude Daily",
            "Take a few minutes each day to write down three things you are grateful for. This simple practice can significantly improve your mental well-being.",
            "Gratitude practice is scientifically proven to increase happiness and reduce stress levels.", jane);

        Advice advice3 = createAdvice("Learn Something New Every Day",
            "Dedicate at least 30 minutes daily to learning something new, whether it's reading, watching educational videos, or taking online courses.",
            "Continuous learning keeps your mind sharp and opens up new opportunities in life and career.", john);

        Advice advice4 = createAdvice("Exercise Regularly",
            "Incorporate physical activity into your daily routine. Even a 20-minute walk can make a significant difference to your health and mood.",
            "Regular exercise is one of the most effective ways to improve both physical and mental health.", mike);

        Advice advice5 = createAdvice("Manage Your Finances Wisely",
            "Create a budget, track your expenses, and save at least 20% of your income. Financial stability reduces stress and provides security.",
            "Good financial habits are essential for long-term security and peace of mind.", jane);

        Advice advice6 = createAdvice("Build Strong Relationships",
            "Invest time in building and maintaining meaningful relationships with family and friends. Quality relationships are key to happiness.",
            "Strong social connections are one of the most important factors for a fulfilling life.", mike);

        Advice advice7 = createAdvice("Practice Mindfulness",
            "Take time to be present in the moment. Mindfulness meditation can help reduce anxiety and improve focus.",
            "Mindfulness practices have been shown to have numerous mental health benefits.", john);

        Advice advice8 = createAdvice("Set Clear Goals",
            "Define specific, measurable, achievable, relevant, and time-bound (SMART) goals for yourself. Clear goals provide direction and motivation.",
            "Goal setting is a powerful tool for personal and professional development.", jane);

        // Create ratings
        createRating(jane, advice1, 5, "This advice changed my life! I've been waking up at 5 AM for a month now and feel so much more productive.");
        createRating(mike, advice1, 4, "Good advice, though it took me a while to adjust to the early schedule.");

        createRating(john, advice2, 5, "Gratitude practice has made me so much happier. Highly recommend!");
        createRating(mike, advice2, 5, "Simple but powerful. I notice the difference in my mood every day.");
        createRating(admin, advice2, 4, "Great advice for mental well-being.");

        createRating(jane, advice3, 4, "Learning new things daily has opened up so many opportunities for me.");

        createRating(admin, advice4, 5, "Exercise is indeed crucial. I feel so much better since I started my daily routine.");
        createRating(john, advice4, 5, "Can't agree more! Regular exercise has improved my energy levels significantly.");
        createRating(jane, advice4, 4, "Good advice, though finding time for exercise can be challenging.");
        createRating(mike, advice4, 5, "Exercise has been a game-changer for my mental health.");

        createRating(admin, advice5, 4, "Financial planning is so important. This advice helped me get started.");
        createRating(john, advice5, 4, "Budgeting has given me so much peace of mind.");

        createRating(admin, advice6, 5, "Relationships are everything. This advice is spot on.");
        createRating(john, advice6, 4, "Quality over quantity when it comes to relationships.");
        createRating(jane, advice6, 5, "Building strong relationships has made me so much happier.");

        createRating(admin, advice7, 4, "Mindfulness has helped me manage stress much better.");
        createRating(mike, advice7, 5, "Meditation changed my life. Highly recommend this advice.");

        createRating(admin, advice8, 4, "SMART goals have helped me achieve so much more.");
    }

    private User createUser(String username, String email, String password, String firstName, String lastName, Set<Role> roles) {
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .roles(roles)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();
        return userRepository.save(user);
    }

    private Advice createAdvice(String title, String message, String description, User author) {
        Advice advice = Advice.builder()
                .title(title)
                .message(message)
                .description(description)
                .author(author)
                .averageRating(0.0)
                .ratingCount(0)
                .build();
        return adviceRepo.save(advice);
    }

    private Rating createRating(User user, Advice advice, Integer rating, String comment) {
        Rating ratingEntity = Rating.builder()
                .user(user)
                .advice(advice)
                .rating(rating)
                .comment(comment)
                .build();
        
        Rating savedRating = ratingRepository.save(ratingEntity);
        
        // Update advice rating statistics
        updateAdviceRating(advice);
        
        return savedRating;
    }

    private void updateAdviceRating(Advice advice) {
        Double averageRating = ratingRepository.findAverageRatingByAdvice(advice);
        Long ratingCount = ratingRepository.countByAdvice(advice);
        
        advice.setAverageRating(averageRating != null ? averageRating : 0.0);
        advice.setRatingCount(ratingCount.intValue());
        
        adviceRepo.save(advice);
    }
}
