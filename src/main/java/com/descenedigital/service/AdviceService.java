package com.descenedigital.service;

import com.descenedigital.domain.entity.Advice;
import com.descenedigital.domain.repo.AdviceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdviceService {
    private final AdviceRepository adviceRepository;

    public AdviceService(AdviceRepository adviceRepository) {
        this.adviceRepository = adviceRepository;
    }

    /**
     * Creates a new advice owned by the given user id.
     */
    @Transactional
    public Advice create(Advice advice, Long ownerId) {
        advice.setCreatedById(ownerId);
        advice.setActive(true);
        return adviceRepository.save(advice);
    }

    /**
     * Updates an existing advice if permitted by caller.
     */
    @Transactional
    public Advice update(Advice existing) {
        return adviceRepository.save(existing);
    }

    /**
     * Soft-deletes an advice by setting isActive to false.
     */
    @Transactional
    public void softDelete(Advice advice) {
        advice.setActive(false);
        adviceRepository.save(advice);
    }

    /**
     * Retrieves advice page by specification.
     */
    @Transactional(readOnly = true)
    public Page<Advice> search(Specification<Advice> spec, Pageable pageable) {
        return adviceRepository.findAll(spec, pageable);
    }

    /**
     * Gets an advice by id.
     */
    @Transactional(readOnly = true)
    public Optional<Advice> findById(Long id) {
        return adviceRepository.findById(id);
    }

    /**
     * Computes average rating for advice id.
     */
    @Transactional(readOnly = true)
    public double averageRating(Long adviceId) {
        return adviceRepository.computeAverageRating(adviceId).orElse(0.0);
    }
}


