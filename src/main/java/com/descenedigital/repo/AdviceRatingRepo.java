package com.descenedigital.repo;

import com.descenedigital.entity.AdviceRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdviceRatingRepo extends JpaRepository<AdviceRating, Long> {

    Optional<AdviceRating> findByAdviceIdAndUserId(Long adviceId, Long userId);

    // avg + count (kept simple)
    @Query("select coalesce(avg(r.stars), 0), count(r) " +
            "from AdviceRating r where r.advice.id = :adviceId")
    Object stats(Long adviceId); // will be an Object[]
}
