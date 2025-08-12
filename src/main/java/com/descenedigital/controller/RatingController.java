package com.descenedigital.controller;

import com.descenedigital.dto.AdviceDto;
import com.descenedigital.dto.RatingDto;
import com.descenedigital.mapper.AdviceMapper;
import com.descenedigital.model.Advice;
import com.descenedigital.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Rating API", description = "API for managing ratings on advice")
@RestController
@RequestMapping(path = "/api/advice")
public class RatingController {
    private final AdviceMapper adviceMapper;
    private final RatingService ratingService;

    public RatingController(AdviceMapper adviceMapper, RatingService ratingService){
        this.adviceMapper = adviceMapper;
        this.ratingService = ratingService;
    }

    @Operation(summary = "Rate an advice", description = "Add a rating to an existing advice by its ID")
    @PostMapping("/{id}/rate")
    public AdviceDto postRating(
            @Parameter(description = "ID of the advice to rate", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RatingDto ratingDto) {
        Advice ratedAdvice = ratingService.addRating(id, ratingDto.rating());
        return adviceMapper.toDto(ratedAdvice);
    }

    @Operation(summary = "Get top rated advice", description = "Retrieve the top 5 advice entries sorted by rating")
    @GetMapping("/top-rated")
    public List<AdviceDto> getTopRatedAdvice() {
        Page<Advice> topRatedPage = ratingService.getTopRatedAdvice(PageRequest.of(0, 5)); // top 5
        return topRatedPage.stream()
                .map(adviceMapper::toDto)
                .toList();
    }
}

