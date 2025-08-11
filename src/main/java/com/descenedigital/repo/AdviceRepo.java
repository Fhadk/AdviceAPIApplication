package com.descenedigital.repo;

import com.descenedigital.model.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdviceRepo extends JpaRepository<Advice, Long> {
    Page<Advice> findByMessageContainingIgnoreCase(String keyword, Pageable pageable);
}
