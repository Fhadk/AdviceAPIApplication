package com.descenedigital.service;

import com.descendigital.enums.Role;
import com.descenedigital.exception.ResourceNotFoundException;
import com.descenedigital.exception.*;
import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepository;
import com.descenedigital.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdviceService {

    private final AdviceRepository adviceRepository;
    private final UserRepository userRepository;

    public AdviceService(AdviceRepository adviceRepository,UserRepository userRepository) {
    	this.adviceRepository = adviceRepository;
    	this.userRepository = userRepository;
    }
    public Map<String, Object> createAdvice(Map<String, String> request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Advice advice = new Advice();
        advice.setMessage(request.get("message"));
        advice.setUser(user);
        
        Advice savedAdvice = adviceRepository.save(advice);
        return mapToAdviceResponse(savedAdvice);
    }

    public Page<Map<String, Object>> getAllAdvice(Pageable pageable) {
        return adviceRepository.findAll(pageable)
                .map(this::mapToAdviceResponse);
    }

    public Map<String, Object> getAdviceById(Long id) {
        Advice advice = adviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advice not found"));
        return mapToAdviceDetailResponse(advice);
    }

    public Map<String, Object> updateAdvice(Long id, Map<String, String> request) {
        Advice advice = adviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advice not found"));
        
        advice.setMessage(request.get("message"));
        Advice updatedAdvice = adviceRepository.save(advice);
        return mapToAdviceResponse(updatedAdvice);
    }

    public void deleteAdvice(Long id) {
        if (!adviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Advice not found");
        }
        adviceRepository.deleteById(id);
    }

    public Map<String, Object> rateAdvice(Long adviceId, Map<String, Integer> request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Advice advice = adviceRepository.findById(adviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Advice not found"));

        if (user.getRole() == Role.ROLE_ADMIN && 
            advice.getUser() != null && 
            advice.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Admins cannot rate their own advice");
        }

        boolean alreadyRated = advice.getRatings().stream()
                .anyMatch(r -> r.getUser().getId().equals(user.getId()));
        
        if (alreadyRated) {
            throw new UnauthorizedException("You have already rated this advice");
        }

        Rating rating = new Rating();
        rating.setScore(request.get("score"));
        rating.setUser(user);
        rating.setAdvice(advice);
        
        advice.getRatings().add(rating);
        adviceRepository.save(advice);

        return mapToRatingResponse(rating, user);
    }

    public List<Map<String, Object>> getTopRatedAdvice() {
        return adviceRepository.findAll().stream()
                .filter(a -> a.getRatings() != null && !a.getRatings().isEmpty())
                .sorted(Comparator.comparingDouble(this::calculateAverageRating).reversed())
                .limit(10)
                .map(this::mapToTopRatedResponse)
                .collect(Collectors.toList());
    }

    // Helper methods
    private Map<String, Object> mapToAdviceResponse(Advice advice) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", advice.getId());
        response.put("message", advice.getMessage());
        response.put("author", advice.getUser() != null ? advice.getUser().getUsername() : null);
        return response;
    }

    
    private Map<String, Object> mapToAdviceDetailResponse(Advice advice) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", advice.getId());
        response.put("message", advice.getMessage());
        response.put("author", advice.getUser() != null ? advice.getUser().getUsername() : null);
        response.put("averageRating", calculateAverageRating(advice));
        response.put("ratings", mapToRatingResponses(advice.getRatings()));
        return response;
    }

    private List<Map<String, Object>> mapToRatingResponses(List<Rating> ratings) {
        return ratings.stream()
                .map(r -> {
                    Map<String, Object> ratingMap = new HashMap<>();
                    ratingMap.put("id", r.getId());
                    ratingMap.put("score", r.getScore());
                    ratingMap.put("ratedBy", r.getUser().getUsername());
                    return ratingMap;
                })
                .collect(Collectors.toList());
    }

    private Map<String, Object> mapToTopRatedResponse(Advice advice) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", advice.getId());
        response.put("message", advice.getMessage());
        response.put("author", advice.getUser() != null ? advice.getUser().getUsername() : null);
        response.put("averageRating", calculateAverageRating(advice));
        response.put("ratingCount", advice.getRatings().size());
        return response;
    }

    private Map<String, Object> mapToRatingResponse(Rating rating, User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", rating.getId());
        response.put("score", rating.getScore());
        response.put("ratedBy", user.getUsername());
        response.put("message", "Rating submitted successfully");
        return response;
    }

    private double calculateAverageRating(Advice advice) {
        if (advice.getRatings() == null || advice.getRatings().isEmpty()) {
            return 0.0;
        }
        return advice.getRatings().stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);
    }
}