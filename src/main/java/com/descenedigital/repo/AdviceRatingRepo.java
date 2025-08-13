package com.descenedigital.repo;

import com.descenedigital.model.Advice;
import com.descenedigital.model.AdviceRating;
import com.descenedigital.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface AdviceRatingRepo extends JpaRepository<AdviceRating, Long> {
    Optional<AdviceRating> findByAdviceAndUser(Advice advice, User user);

    List<AdviceRating> findByAdvice(Advice advice);
    @Query("SELECT ar.advice, AVG(ar.rating) as avgRating " +
            "FROM AdviceRating ar " +
            "GROUP BY ar.advice " +
            "ORDER BY avgRating DESC")
    List<Object[]> findTopRatedAdvice(Pageable pageable);

}

