package com.descenedigital.controller;

import com.descenedigital.dto.AdviceDto;
import com.descenedigital.dto.RatingDto;
import com.descenedigital.mapper.AdviceMapper;
import com.descenedigital.model.Advice;
import com.descenedigital.service.AdviceService;
import com.descenedigital.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/advice")
public class RatingController {
    private final AdviceMapper adviceMapper;
    private final RatingService ratingService;

    public  RatingController(AdviceMapper adviceMapper, RatingService ratingService){
        this.adviceMapper = adviceMapper;
        this.ratingService = ratingService;
    }

    // Rate an advice
    @PostMapping("/{id}/rate")
    public AdviceDto postRating(@PathVariable Long id, @Valid @RequestBody RatingDto ratingDto) {
        Advice ratedAdvice = ratingService.addRating(id, ratingDto.rating());
        return adviceMapper.toDto(ratedAdvice);
    }

}
