package com.descenedigital.service;

import com.descenedigital.model.Advice;
import com.descenedigital.repo.AdviceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdviceService {

    private final AdviceRepo adviceRepo;

    /**
     * Create a new advice entry
     */
    public Advice createAdvice(Advice advice) {
        return adviceRepo.save(advice);
    }

    /**
     * Get all advice entries
     */
    public List<Advice> getAllAdvice() {
        return adviceRepo.findAll();
    }

    /**
     * Get advice by ID
     */
    public Optional<Advice> getAdviceById(Long id) {
        return adviceRepo.findById(id);
    }

    /**
     * Update existing advice
     */
    public Optional<Advice> updateAdvice(Long id, Advice adviceDetails) {
        return adviceRepo.findById(id)
                .map(existingAdvice -> {
                    existingAdvice.setMessage(adviceDetails.getMessage());
                    return adviceRepo.save(existingAdvice);
                });
    }

    /**
     * Delete advice by ID
     */
    public boolean deleteAdvice(Long id) {
        if (adviceRepo.existsById(id)) {
            adviceRepo.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get advice count
     */
    public long getAdviceCount() {
        return adviceRepo.count();
    }
}