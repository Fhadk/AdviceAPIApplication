package com.descenedigital.repo;
import com.descenedigital.model.Advice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AdviceRepository extends JpaRepository<Advice, Long> {
    @Query("SELECT a FROM Advice a WHERE SIZE(a.ratings) >= 5 ORDER BY (SELECT AVG(r.score) FROM Rating r WHERE r.advice = a) DESC")
    List<Advice> findTopRatedAdvice();
    @Query("SELECT a FROM Advice a LEFT JOIN a.ratings r GROUP BY a.id ORDER BY AVG(r.score) DESC")
    List<Advice> findTopRatedAdvices();

    @Query("SELECT a FROM Advice a LEFT JOIN a.ratings r GROUP BY a.id ORDER BY AVG(r.score) ASC")
    List<Advice> findLowRatedAdvices();
}