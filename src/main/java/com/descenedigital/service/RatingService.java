package com.descenedigital.service;

import com.descenedigital.domain.entity.AdviceRating;
import com.descenedigital.domain.repo.AdviceRatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingService {
    private final AdviceRatingRepository ratingRepository;

    public RatingService(AdviceRatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Upserts a rating for the given advice and user.
     */
    @Transactional
    public AdviceRating rate(Long adviceId, Long userId, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        AdviceRating entity = ratingRepository.findByAdviceIdAndUserId(adviceId, userId)
                .orElseGet(AdviceRating::new);
        entity.setAdviceId(adviceId);
        entity.setUserId(userId);
        entity.setRating(rating);
        return ratingRepository.save(entity);
    }
}


