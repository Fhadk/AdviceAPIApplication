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
           "LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.message) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Advice> findBySearchTerm(@Param("search") String search, Pageable pageable);

    @Query("SELECT a FROM Advice a WHERE a.averageRating >= :minRating")
    Page<Advice> findByMinimumRating(@Param("minRating") Double minRating, Pageable pageable);

    @Query("SELECT a FROM Advice a ORDER BY a.averageRating DESC")
    Page<Advice> findTopRated(Pageable pageable);

    @Query("SELECT a FROM Advice a ORDER BY a.createdAt DESC")
    Page<Advice> findLatest(Pageable pageable);

    @Query("SELECT a FROM Advice a WHERE a.author.id = :authorId")
    Page<Advice> findByAuthorId(@Param("authorId") Long authorId, Pageable pageable);
}