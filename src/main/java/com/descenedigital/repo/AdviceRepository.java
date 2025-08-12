package com.descenedigital.repo;
import com.descenedigital.model.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdviceRepository extends JpaRepository<Advice, Long> {

    // Get paginated advice with average rating
    @Query("SELECT a FROM Advice a LEFT JOIN FETCH a.ratings")
    Page<Advice> findAllWithRatings(Pageable pageable);

    // Get top-rated advice (minimum 5 ratings)
    @Query("""
        SELECT a FROM Advice a 
        WHERE SIZE(a.ratings) >= 5 
        ORDER BY (
            SELECT AVG(r.score) FROM Rating r WHERE r.advice = a
        ) DESC
        LIMIT 10
        """)
    List<Advice> findTopRatedAdvice();

    // Find advice by creator user
    List<Advice> findByUser_Id(Long userId);
}