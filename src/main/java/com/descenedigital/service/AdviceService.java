package com.descenedigital.service;

import com.descenedigital.dto.advice.AdviceRequest;
import com.descenedigital.dto.advice.AdviceResponse;
import com.descenedigital.mapper.AdviceMapper;
import com.descenedigital.model.Advice;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdviceService {

    private final AdviceRepo adviceRepo;
    private final UserRepository userRepository;
    private final AdviceMapper adviceMapper;

    public Page<AdviceResponse> getAllAdvice(Pageable pageable) {
        return adviceRepo.findAll(pageable)
                .map(adviceMapper::toResponse);
    }

    public AdviceResponse getAdviceById(Long id) {
        Advice advice = adviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Advice not found with id: " + id));
        return adviceMapper.toResponse(advice);
    }

    public AdviceResponse createAdvice(AdviceRequest request) {
        User currentUser = getCurrentUser();
        Advice advice = adviceMapper.toEntity(request);
        advice.setAuthor(currentUser);

        Advice savedAdvice = adviceRepo.save(advice);
        return adviceMapper.toResponse(savedAdvice);
    }

    public AdviceResponse updateAdvice(Long id, AdviceRequest request) {
        Advice advice = adviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Advice not found with id: " + id));

        User currentUser = getCurrentUser();
        if (!advice.getAuthor().getId().equals(currentUser.getId()) &&
            !currentUser.getRoles().stream().anyMatch(role -> role.getValue().equals("ADMIN"))) {
            throw new RuntimeException("You can only update your own advice");
        }

        adviceMapper.updateEntity(request, advice);
        Advice updatedAdvice = adviceRepo.save(advice);
        return adviceMapper.toResponse(updatedAdvice);
    }

    public void deleteAdvice(Long id) {
        Advice advice = adviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Advice not found with id: " + id));

        User currentUser = getCurrentUser();
        if (!advice.getAuthor().getId().equals(currentUser.getId()) &&
            !currentUser.getRoles().stream().anyMatch(role -> role.getValue().equals("ADMIN"))) {
            throw new RuntimeException("You can only delete your own advice");
        }

        adviceRepo.delete(advice);
    }

    public Page<AdviceResponse> searchAdvice(String searchTerm, Pageable pageable) {
        return adviceRepo.findBySearchTerm(searchTerm, pageable)
                .map(adviceMapper::toResponse);
    }

    public Page<AdviceResponse> getAdviceByAuthor(Long authorId, Pageable pageable) {
        return adviceRepo.findByAuthorId(authorId, pageable)
                .map(adviceMapper::toResponse);
    }

    public Page<AdviceResponse> getTopRatedAdvice(Pageable pageable) {
        return adviceRepo.findTopRated(pageable)
                .map(adviceMapper::toResponse);
    }

    public Page<AdviceResponse> getLatestAdvice(Pageable pageable) {
        return adviceRepo.findLatest(pageable)
                .map(adviceMapper::toResponse);
    }

    public Page<AdviceResponse> getAdviceByMinRating(Double minRating, Pageable pageable) {
        return adviceRepo.findByMinimumRating(minRating, pageable)
                .map(adviceMapper::toResponse);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}