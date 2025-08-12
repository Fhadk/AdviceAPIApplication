package com.descenedigital.repo;

import com.descenedigital.model.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdviceRepository extends JpaRepository<Advice, Long> {
    Page<Advice> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String t, String c, Pageable p);

    @Query("SELECT a FROM Advice a LEFT JOIN a.ratings r GROUP BY a ORDER BY AVG(r.value) DESC")
    Page<Advice> findTopRated(Pageable pageable);
}