package com.descenedigital.service;

import com.descenedigital.model.Advice;
import com.descenedigital.repo.AdviceRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdviceService {

    private final AdviceRepo adviceRepo;

    public AdviceService(AdviceRepo adviceRepo) {
        this.adviceRepo = adviceRepo;
    }

    public Advice createAdvice(Advice advice) {
        return adviceRepo.save(advice);
    }

    public Page<Advice> getAllAdvice(Pageable pageable) {
        return adviceRepo.findAll(pageable);
    }

    public Page<Advice> searchAdvice(String keyword, Pageable pageable) {
        return adviceRepo.findByMessageContainingIgnoreCase(keyword, pageable);
    }

    public Page<Advice> getTopRatedAdvice(Pageable pageable) {
        return adviceRepo.findTopRated(pageable);
    }

    public Optional<Advice> getAdviceById(Long id) {
        return adviceRepo.findById(id);
    }

    public Optional<Advice> updateAdvice(Long id, Advice updatedAdvice) {
        return adviceRepo.findById(id).map(existing -> {
            existing.setMessage(updatedAdvice.getMessage());
            existing.setCreatedBy(updatedAdvice.getCreatedBy());
            return adviceRepo.save(existing);
        });
    }

    public boolean deleteAdvice(Long id) {
        if (adviceRepo.existsById(id)) {
            adviceRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Advice> rateAdvice(Long id, double rating) {
        return adviceRepo.findById(id).map(advice -> {
            advice.addRating(rating);
            return adviceRepo.save(advice);
        });
    }
}
