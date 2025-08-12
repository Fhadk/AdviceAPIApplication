package com.descenedigital.repo;

import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByAdviceAndUser(Advice advice, User user);
}
