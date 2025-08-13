package com.descenedigital.repo;

import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    Optional<Rating> findByUserAndAdvice(User user, Advice advice);
    
    Page<Rating> findByAdvice(Advice advice, Pageable pageable);
    
    Page<Rating> findByUser(User user, Pageable pageable);
    
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.advice = :advice")
    Double findAverageRatingByAdvice(@Param("advice") Advice advice);
    
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.advice = :advice")
    Long countByAdvice(@Param("advice") Advice advice);
    
    @Query("SELECT r FROM Rating r WHERE r.advice.id = :adviceId")
    Page<Rating> findByAdviceId(@Param("adviceId") Long adviceId, Pageable pageable);
    
    @Query("SELECT r FROM Rating r WHERE r.user.id = :userId")
    Page<Rating> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    boolean existsByUserAndAdvice(User user, Advice advice);
}
