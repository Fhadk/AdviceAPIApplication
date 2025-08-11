package com.descenedigital.repo;

import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    Optional<Rating> findByUserAndAdvice(User user, Advice advice);
    
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.advice = :advice")
    Double calculateAverageRating(@Param("advice") Advice advice);
    
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.advice = :advice")
    Integer countRatingsByAdvice(@Param("advice") Advice advice);
} 