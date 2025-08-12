package com.descenedigital.repo;


import com.descenedigital.model.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface AdviceRepo extends JpaRepository<Advice, Long> {
    @Query("""
        SELECT a FROM Advice a
        WHERE a.ratingCount > 0
        ORDER BY (a.ratingSum * 1.0 / a.ratingCount) DESC
        """)
    Page<Advice> findTopRated(Pageable pageable);

    Page<Advice> findByMessageContainingIgnoreCase(String keyword, Pageable pageable);
}