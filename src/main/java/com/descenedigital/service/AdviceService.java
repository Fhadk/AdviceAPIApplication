package com.descenedigital.service;

import com.descenedigital.dto.AdviceDTO;
import com.descenedigital.model.Advice;
import com.descenedigital.repo.AdviceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdviceService {
    private final AdviceRepo adviceRepo;

    public AdviceDTO createAdvice(AdviceDTO dto) {
        Advice advice = Advice.builder().message(dto.getMessage()).build();
        advice = adviceRepo.save(advice);
        dto.setId(advice.getId());
        return dto;
    }

    public Optional<AdviceDTO> getAdviceById(Long id) {
        return adviceRepo.findById(id).map(advice -> AdviceDTO.builder().id(advice.getId()).message(advice.getMessage()).build());

    }

    public Page<AdviceDTO> getAdvicePage(String keyword, Double minRating, String sortBy, String direction, int page, int size) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return adviceRepo.findByFilters(keyword, minRating, pageable)
                .map(advice -> AdviceDTO.builder()
                        .id(advice.getId())
                        .message(advice.getMessage())
                        .averageRating(advice.getAverageRating()).build());

    }

    public Optional<AdviceDTO> updateAdvice(Long id, AdviceDTO dto) {
        return adviceRepo.findById(id).map(advice -> {
            advice.setMessage(dto.getMessage());
            adviceRepo.save(advice);
            dto.setId(advice.getId());
            return dto;
        });
    }

    public boolean deleteAdvice(Long id) {
        if (adviceRepo.existsById(id)) {
            adviceRepo.deleteById(id);
            return true;
        }
        return false;
    }
}