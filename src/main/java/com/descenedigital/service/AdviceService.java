package com.descenedigital.service;

import com.descenedigital.dto.CreateAdviceRequest;
import com.descenedigital.mapper.AdviceMapper;
import com.descenedigital.model.Advice;
import com.descenedigital.repo.AdviceRepo;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdviceService {

    private final AdviceRepo repo;
    private final AdviceMapper mapper;

    public AdviceService(AdviceRepo repo, AdviceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public Page<Advice> list(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return repo.findAll(pageable);
        } else {
            return repo.findByMessageContainingIgnoreCase(keyword, pageable);
        }
    }

    public Optional<Advice> get(Long id) {
        return repo.findById(id);
    }

    public Advice create(CreateAdviceRequest req, String username) {
        Advice a = mapper.toEntity(req);
        a.setCreatedBy(username);
        return repo.save(a);
    }

    public Advice update(Long id, CreateAdviceRequest req, Authentication auth) {
        var a = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !username.equals(a.getCreatedBy())) {
            throw new SecurityException("Not allowed");
        }
        mapper.updateFromDto(req, a);
        return repo.save(a);
    }

    public void delete(Long id, Authentication auth) {
        var a = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) throw new SecurityException("Only admin can delete");
        repo.delete(a);
    }

    @Transactional
    public Advice rate(Long id, int rating, String rater) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("rating must be 1..5");
        var a = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        int currentCount = a.getRatingCount() == null ? 0 : a.getRatingCount();
        double currentAvg = a.getAverageRating() == null ? 0.0 : a.getAverageRating();

        int newCount = currentCount + 1;
        double newAvg = (currentAvg * currentCount + rating) / newCount;
        a.setRatingCount(newCount);
        a.setAverageRating(newAvg);
        return repo.save(a);
    }
}
