package com.descenedigital.service;

import com.descenedigital.dto.CreateAdviceRequest;
import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepository;
import com.descenedigital.repo.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.OptionalDouble;

@Service
public class AdviceService {
    private final AdviceRepository adviceRepository;
    private final RatingRepository ratingRepository;

    public AdviceService(AdviceRepository adviceRepository, RatingRepository ratingRepository) {
        this.adviceRepository = adviceRepository;
        this.ratingRepository = ratingRepository;
    }

    public Advice create(CreateAdviceRequest req, String author) {
        Advice a = new Advice();
        a.setTitle(req.title);
        a.setContent(req.content);
        a.setAuthor(author);
        return adviceRepository.save(a);
    }

    public Page<Advice> search(String q, Pageable pageable) {
        if (q == null || q.isBlank()) return adviceRepository.findAll(pageable);
        return adviceRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(q, q, pageable);
    }

    public OptionalDouble averageRating(Advice a) {
        return a.getRatings().stream().mapToInt(Rating::getValue).average();
    }

    public Page<Advice> topRated(Pageable pageable) {
        return adviceRepository.findTopRated(pageable);
    }

    public Advice get(Long id) { return adviceRepository.findById(id).orElseThrow(() -> new RuntimeException("NotFound")); }

    public Advice update(Long id, CreateAdviceRequest req) {
        Advice a = get(id);
        a.setTitle(req.title);
        a.setContent(req.content);
        a.setUpdatedAt(java.time.Instant.now());
        return adviceRepository.save(a);
    }

    public void delete(Long id) { adviceRepository.deleteById(id); }

    @Transactional
    public Rating rate(Long adviceId, User user, int value) {
        Advice a = get(adviceId);
        var existing = ratingRepository.findByAdviceAndUser(a, user);
        Rating r;
        if (existing.isPresent()) {
            r = existing.get();
            r.setValue(value);
        } else {
            r = new Rating();
            r.setAdvice(a);
            r.setUser(user);
            r.setValue(value);
        }
        return ratingRepository.save(r);
    }
}