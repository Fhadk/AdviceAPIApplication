package com.descenedigital.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepository;
import com.descenedigital.repo.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/advice")
public class AdviceController {

    private final AdviceRepository adviceRepository;
    private final UserRepository userRepository;

    public AdviceController(AdviceRepository adviceRepository, UserRepository userRepository) {
        this.adviceRepository = adviceRepository;
        this.userRepository = userRepository;
    }

    // Only ADMIN can post new advice
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Advice createAdvice(@RequestBody Map<String, String> request) {
        Advice advice = new Advice();
        advice.setMessage(request.get("message"));
        return adviceRepository.save(advice);
    }

    // Both USER and ADMIN can view all advice (paginated)
    @GetMapping
    public Page<Advice> getAllAdvice(Pageable pageable) {
        return adviceRepository.findAll(pageable);
    }

    // Only ADMIN can update advice
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Advice> updateAdvice(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        return adviceRepository.findById(id)
                .map(advice -> {
                    advice.setMessage(request.get("message"));
                    return ResponseEntity.ok(adviceRepository.save(advice));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Only ADMIN can delete advice
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteAdvice(@PathVariable Long id) {
        adviceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // USER can rate advice (but can't rate their own if they're ADMIN)
    @PostMapping("/{id}/rate")
    public ResponseEntity<String> rateAdvice(
            @PathVariable Long id,
            @RequestParam int score
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Advice advice = adviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advice not found"));

        // Prevent ADMIN from rating their own advice
        if (user.getRole().equals("ADMIN") && advice.getUser() != null && advice.getUser().equals(user)) {
            return ResponseEntity.badRequest().body("Admins cannot rate their own advice");
        }

        Rating rating = new Rating();
        rating.setScore(score);
        rating.setUser(user);
        advice.getRatings().add(rating);
        adviceRepository.save(advice);

        return ResponseEntity.ok("Rating added successfully");
    }

    // Anyone can get top-rated advice (sorted by average rating)
    @GetMapping("/top-rated")
    public List<Advice> getTopRatedAdvice() {
        return adviceRepository.findAll().stream()
                .filter(a -> a.getRatings() != null && !a.getRatings().isEmpty())
                .sorted(Comparator.comparingDouble(this::calculateAverageRating).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    private double calculateAverageRating(Advice advice) {
        return advice.getRatings().stream()
                .mapToInt(r -> r.getScore())
                .average()
                .orElse(0.0);
    }
}