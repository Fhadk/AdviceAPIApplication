package com.descenedigital.service;

import com.descenedigital.dto.rating.RatingRequest;
import com.descenedigital.dto.rating.RatingResponse;
import com.descenedigital.mapper.RatingMapper;
import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.RatingRepository;
import com.descenedigital.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingService {

    private final RatingRepository ratingRepository;
    private final AdviceRepo adviceRepo;
    private final UserRepository userRepository;
    private final RatingMapper ratingMapper;

    public RatingResponse createOrUpdateRating(Long adviceId, RatingRequest request) {
        User currentUser = getCurrentUser();
        Advice advice = adviceRepo.findById(adviceId)
                .orElseThrow(() -> new RuntimeException("Advice not found with id: " + adviceId));

        // Check if user already rated this advice
        Rating existingRating = ratingRepository.findByUserAndAdvice(currentUser, advice)
                .orElse(null);

        Rating rating;
        if (existingRating != null) {
            // Update existing rating
            ratingMapper.updateEntity(request, existingRating);
            rating = ratingRepository.save(existingRating);
        } else {
            // Create new rating
            rating = ratingMapper.toEntity(request);
            rating.setUser(currentUser);
            rating.setAdvice(advice);
            rating = ratingRepository.save(rating);
        }

        // Update advice average rating
        updateAdviceRating(advice);

        return ratingMapper.toResponse(rating);
    }

    public RatingResponse getRatingById(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + id));
        return ratingMapper.toResponse(rating);
    }

    public Page<RatingResponse> getRatingsByAdvice(Long adviceId, Pageable pageable) {
        return ratingRepository.findByAdviceId(adviceId, pageable)
                .map(ratingMapper::toResponse);
    }

    public Page<RatingResponse> getRatingsByUser(Long userId, Pageable pageable) {
        return ratingRepository.findByUserId(userId, pageable)
                .map(ratingMapper::toResponse);
    }

    public Page<RatingResponse> getCurrentUserRatings(Pageable pageable) {
        User currentUser = getCurrentUser();
        return ratingRepository.findByUserId(currentUser.getId(), pageable)
                .map(ratingMapper::toResponse);
    }

    public void deleteRating(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + id));

        User currentUser = getCurrentUser();
        if (!rating.getUser().getId().equals(currentUser.getId()) && 
            !currentUser.getRoles().stream().anyMatch(role -> role.getValue().equals("ADMIN"))) {
            throw new RuntimeException("You can only delete your own ratings");
        }

        Advice advice = rating.getAdvice();
        ratingRepository.delete(rating);
        
        // Update advice average rating after deletion
        updateAdviceRating(advice);
    }

    private void updateAdviceRating(Advice advice) {
        Double averageRating = ratingRepository.findAverageRatingByAdvice(advice);
        Long ratingCount = ratingRepository.countByAdvice(advice);
        
        advice.setAverageRating(averageRating != null ? averageRating : 0.0);
        advice.setRatingCount(ratingCount.intValue());
        
        adviceRepo.save(advice);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
