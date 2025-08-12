package com.descenedigital.repo;

import com.descenedigital.entity.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdviceRepo extends JpaRepository<Advice, Long> {
    Page<Advice> findAllByMessageContainingIgnoreCase(String q, Pageable pageable);

    @Query("SELECT a FROM Advice a ORDER BY a.avgRating DESC, a.ratingsCount DESC, a.createdAt DESC")
    Page<Advice> topRated(Pageable pageable);
}