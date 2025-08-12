package com.descenedigital.repo;

import com.descenedigital.model.Advice;
import com.descenedigital.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdviceRepo extends JpaRepository<Advice, Long> {
    
    Page<Advice> findByAuthor(User author, Pageable pageable);
    
    @Query("SELECT a FROM Advice a WHERE " +
           "(:category IS NULL OR LOWER(a.category) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
           "(:message IS NULL OR LOWER(a.message) LIKE LOWER(CONCAT('%', :message, '%')))")
    Page<Advice> findByFilters(@Param("category") String category, 
                              @Param("message") String message, 
                              Pageable pageable);
    
    @Query("SELECT a FROM Advice a ORDER BY a.averageRating DESC, a.ratingCount DESC")
    Page<Advice> findTopRated(Pageable pageable);
}