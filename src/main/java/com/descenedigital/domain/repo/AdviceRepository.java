package com.descenedigital.domain.repo;

import com.descenedigital.domain.entity.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdviceRepository extends JpaRepository<Advice, Long>, JpaSpecificationExecutor<Advice> {

    @Query("select avg(ar.rating) from AdviceRating ar where ar.adviceId = :adviceId")
    Optional<Double> computeAverageRating(@Param("adviceId") Long adviceId);
}


