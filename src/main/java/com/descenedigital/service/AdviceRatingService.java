package com.descenedigital.service;

import com.descenedigital.dto.AdviceDTO;
import com.descenedigital.model.Advice;
import com.descenedigital.model.AdviceRating;
import com.descenedigital.model.User;
import com.descenedigital.repo.AdviceRatingRepo;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class AdviceRatingService {
    private final AdviceRatingRepo adviceRatingRepo;
    private final AdviceRepo adviceRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;


    @Transactional
    public AdviceRating giveRating(Long adviceId, Long userId, int rating) {
        Advice advice = adviceRepo.findById(adviceId).orElseThrow(() -> new RuntimeException("Advice with this id not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User with this id not found"));
        AdviceRating adviceRating = adviceRatingRepo.findByAdviceAndUser(advice, user).orElse(AdviceRating.builder().advice(advice).user(user).build());
        adviceRating.setRating(rating);
        return adviceRatingRepo.save(adviceRating);
    }

    public List<AdviceRating> getRatingsForAdvice(Long adviceId) {
        Advice advice = adviceRepo.findById(adviceId).orElseThrow(() -> new RuntimeException("Advice with id not found"));
        return adviceRatingRepo.findByAdvice(advice);
    }

    public double getAverageRating(Long adviceId) {
        List<AdviceRating> ratings = getRatingsForAdvice(adviceId);
        if (ratings.isEmpty())
            return 0;
        return ratings.stream().mapToInt(AdviceRating::getRating).average().orElse(0);
    }
    public List<AdviceDTO> getTopRatedAdvice(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> results = adviceRatingRepo.findTopRatedAdvice(pageable);

        return results.stream().map(obj -> {
            Advice advice = (Advice) obj[0];
            Double avgRating = (Double) obj[1];

            AdviceDTO dto = modelMapper.map(advice, AdviceDTO.class);
            dto.setAverageRating(avgRating);
            return dto;
        }).collect(Collectors.toList());
    }


}
