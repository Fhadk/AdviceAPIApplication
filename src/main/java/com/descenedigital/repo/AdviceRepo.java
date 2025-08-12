package com.descenedigital.repo;

import com.descenedigital.model.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdviceRepo extends JpaRepository<Advice, Long> {
    @Query(
            """
            SELECT advice FROM Advice advice 
            WHERE (:keyword IS NULL OR LOWER(advice.message) LIKE LOWER(CONCAT('%',:keyword,'%')))
            AND (:minRating IS NULL OR advice.averageRating >= :minRating)
    """)
    Page<Advice> findByFilters(@Param("keyword") String keyword,@Param("minRating") Double minRating, Pageable pageable);
}