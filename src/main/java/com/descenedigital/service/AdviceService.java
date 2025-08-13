package com.descenedigital.service;

import com.descenedigital.model.Advice;
import com.descenedigital.repo.AdviceRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdviceService {
    private final AdviceRepo adviceRepo;

    public AdviceService(AdviceRepo adviceRepo) {
        this.adviceRepo = adviceRepo;
    }

    public Page<Advice> list(Pageable pageable) {
        return adviceRepo.findAll(pageable);
    }

    public Optional<Advice> getById(Long id) {
        return adviceRepo.findById(id);
    }

    @Transactional
    public Advice create(Advice advice) {
        advice.setId(null);
        return adviceRepo.save(advice);
    }

    @Transactional
    public Optional<Advice> update(Long id, Advice updated) {
        return adviceRepo.findById(id).map(existing -> {
            existing.setMessage(updated.getMessage());
            return adviceRepo.save(existing);
        });
    }

    @Transactional
    public void delete(Long id) {
        adviceRepo.deleteById(id);
    }
}