package com.descenedigital.service;

import com.descenedigital.dto.RateReq;
import com.descenedigital.dto.RatingResp;
import com.descenedigital.entity.Advice;
import com.descenedigital.entity.AdviceRating;
import com.descenedigital.entity.User;
import com.descenedigital.repo.AdviceRatingRepo;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingService {

  private final AdviceRepo adviceRepo;
  private final AdviceRatingRepo ratingRepo;
  private final UserRepo userRepo;

  public RatingService(AdviceRepo adviceRepo, AdviceRatingRepo ratingRepo, UserRepo userRepo) {
    this.adviceRepo = adviceRepo;
    this.ratingRepo = ratingRepo;
    this.userRepo = userRepo;
  }

  @Transactional
  public RatingResp rate(Long adviceId, String username, RateReq req) {
    int stars = req.stars();
    if (stars < 1 || stars > 5) {
      throw new IllegalArgumentException("Stars must be 1–5");
    }

    Advice advice = adviceRepo.findById(adviceId)
            .orElseThrow(() -> new IllegalArgumentException("Advice not found: " + adviceId));

    User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

    // upsert (advice,user)
    AdviceRating rating = ratingRepo.findByAdviceIdAndUserId(adviceId, user.getId())
            .orElseGet(() -> AdviceRating.builder().advice(advice).user(user).build());
    rating.setStars(stars);
    ratingRepo.save(rating);

    // refresh stats (simple Object[] handling)
    Object result = ratingRepo.stats(adviceId);
    Object[] row = (Object[]) result;                 // [avg(Double), count(Long)]
    double avg = row[0] == null ? 0.0 : ((Number) row[0]).doubleValue();
    int count   = row[1] == null ? 0   : ((Number) row[1]).intValue();

    advice.setAvgRating(avg);
    advice.setRatingsCount(count);
    adviceRepo.save(advice);

    return new RatingResp(adviceId, avg, count);
  }
}
