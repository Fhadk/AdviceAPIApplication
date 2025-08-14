package com.descenedigital.repo;
import com.descenedigital.model.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AdviceRepository extends JpaRepository<Advice, Long> {
    @Query("SELECT a FROM Advice a WHERE SIZE(a.ratings) >= 5 ORDER BY (SELECT AVG(r.score) FROM Rating r WHERE r.advice = a) DESC")
    List<Advice> findTopRatedAdvice();
}