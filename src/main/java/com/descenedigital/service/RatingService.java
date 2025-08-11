package com.descenedigital.service;

import com.descenedigital.dto.AdviceDto;
import com.descenedigital.model.Advice;
import com.descenedigital.repo.AdviceRepo;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
    private final AdviceRepo adviceRepo;

    public RatingService(AdviceRepo adviceRepo) {
        this.adviceRepo = adviceRepo;
    }


    public Advice addRating(Long id, int rating) {
        Advice advice = adviceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advice not found!"));

        advice.setRatingSum(advice.getRatingSum() + rating);
        advice.setRatingCount(advice.getRatingCount() + 1);

        return adviceRepo.save(advice);
    }

}
