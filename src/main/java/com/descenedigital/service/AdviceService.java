package com.descenedigital.service;


import com.descenedigital.model.Advice;
import com.descenedigital.repo.AdviceRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdviceService {
    private final AdviceRepo adviceRepo;

    public AdviceService(AdviceRepo adviceRepo) {
        this.adviceRepo = adviceRepo;
    }

    // Get all advices
    public List<Advice> ListOfAdvices() {
        return adviceRepo.findAll();
    }

    // Post an advice
    public Advice CreateWorkout(Advice advice) {
        if (null != advice.getId()) {
            throw new IllegalArgumentException("Advice already has an id!");
        }
        if (advice.getMessage() == null || advice.getMessage().isBlank()) {
            throw new IllegalArgumentException("Advice must contain all the fields!");
        }

        return adviceRepo.save(new Advice(
                null,
                advice.getMessage(),
                null,
                null,
                0,
                0
        ));
    }

    // Delete an advice
    public void DeleteAdvice(Long id) {
        adviceRepo.deleteById(id);
    }

    // Update an advice
    public Advice updateAdvice(Long id, Advice advice) {
        Advice existingAdvice = adviceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advice not found!"));
        existingAdvice.setMessage(advice.getMessage());
        return adviceRepo.save(existingAdvice);
    }

    // Get single advice by id
    public Advice getAdviceById(Long id) {
        Advice advice = adviceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advice not found!"));
        return advice;
    }

    // Pagination and filtering

    public Page<Advice> getAdviceFiltered(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return adviceRepo.findAll(pageable);
        }
        return adviceRepo.findByMessageContainingIgnoreCase(keyword, pageable);
    }
}