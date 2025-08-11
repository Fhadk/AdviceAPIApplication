package com.descenedigital.service;

import com.descenedigital.dto.AdviceRequest;
import com.descenedigital.dto.AdviceResponse;
import com.descenedigital.dto.RatingRequest;
import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdviceService {

    private final AdviceRepo adviceRepo;
    private final RatingRepository ratingRepository;
    private final UserService userService;

    public Page<AdviceResponse> getAllAdvice(int page, int size, String sortBy, String sortDir,
                                           String category, String message) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Advice> advicePage;
        if (category != null || message != null) {
            advicePage = adviceRepo.findByFilters(category, message, pageable);
        } else {
            advicePage = adviceRepo.findAll(pageable);
        }

        return advicePage.map(this::mapToResponse);
    }

    public Page<AdviceResponse> getTopRatedAdvice(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Advice> advicePage = adviceRepo.findTopRated(pageable);
        return advicePage.map(this::mapToResponse);
    }

    public Page<AdviceResponse> getAdviceByAuthor(String username, int page, int size) {
        User author = userService.getCurrentUser(username);
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Advice> advicePage = adviceRepo.findByAuthor(author, pageable);
        return advicePage.map(this::mapToResponse);
    }

    public AdviceResponse getAdviceById(Long id) {
        Advice advice = adviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Advice not found with id: " + id));
        return mapToResponse(advice);
    }

    public AdviceResponse createAdvice(AdviceRequest request, String username) {
        User author = userService.getCurrentUser(username);
        
        Advice advice = Advice.builder()
                .message(request.getMessage())
                .category(request.getCategory())
                .author(author)
                .build();

        Advice savedAdvice = adviceRepo.save(advice);
        return mapToResponse(savedAdvice);
    }

    @PreAuthorize("hasRole('ADMIN') or @adviceService.isOwner(#id, authentication.name)")
    public AdviceResponse updateAdvice(Long id, AdviceRequest request, String username) {
        Advice advice = adviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Advice not found with id: " + id));

        advice.setMessage(request.getMessage());
        advice.setCategory(request.getCategory());

        Advice updatedAdvice = adviceRepo.save(advice);
        return mapToResponse(updatedAdvice);
    }

    @PreAuthorize("hasRole('ADMIN') or @adviceService.isOwner(#id, authentication.name)")
    public void deleteAdvice(Long id, String username) {
        Advice advice = adviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Advice not found with id: " + id));
        adviceRepo.delete(advice);
    }

    @Transactional
    public void rateAdvice(Long adviceId, RatingRequest request, String username) {
        User user = userService.getCurrentUser(username);
        Advice advice = adviceRepo.findById(adviceId)
                .orElseThrow(() -> new RuntimeException("Advice not found with id: " + adviceId));

        Optional<Rating> existingRating = ratingRepository.findByUserAndAdvice(user, advice);
        
        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            rating.setRating(request.getRating());
            ratingRepository.save(rating);
        } else {
            Rating rating = Rating.builder()
                    .user(user)
                    .advice(advice)
                    .rating(request.getRating())
                    .build();
            ratingRepository.save(rating);
        }

        updateAdviceRatingStats(advice);
    }

    private void updateAdviceRatingStats(Advice advice) {
        Double averageRating = ratingRepository.calculateAverageRating(advice);
        Integer ratingCount = ratingRepository.countRatingsByAdvice(advice);
        
        advice.setAverageRating(averageRating != null ? averageRating : 0.0);
        advice.setRatingCount(ratingCount);
        adviceRepo.save(advice);
    }

    public boolean isOwner(Long adviceId, String username) {
        return adviceRepo.findById(adviceId)
                .map(advice -> advice.getAuthor().getUsername().equals(username))
                .orElse(false);
    }

    private AdviceResponse mapToResponse(Advice advice) {
        return AdviceResponse.builder()
                .id(advice.getId())
                .message(advice.getMessage())
                .category(advice.getCategory())
                .authorUsername(advice.getAuthor().getUsername())
                .averageRating(advice.getAverageRating())
                .ratingCount(advice.getRatingCount())
                .createdAt(advice.getCreatedAt())
                .updatedAt(advice.getUpdatedAt())
                .build();
    }
}