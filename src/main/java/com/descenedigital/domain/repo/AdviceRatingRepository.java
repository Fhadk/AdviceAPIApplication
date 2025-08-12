package com.descenedigital.domain.repo;

import com.descenedigital.domain.entity.AdviceRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdviceRatingRepository extends JpaRepository<AdviceRating, Long> {
    Optional<AdviceRating> findByAdviceIdAndUserId(Long adviceId, Long userId);
    List<AdviceRating> findByAdviceIdIn(Iterable<Long> adviceIds);
}


