package com.descenedigital.controller;

import com.descenedigital.dto.AdviceDTO;
import com.descenedigital.dto.AdviceRatingDTO;
import com.descenedigital.model.AdviceRating;
import com.descenedigital.service.AdviceRatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advice_ratings")
@RequiredArgsConstructor
@Tag(name = "Advice Ratings", description = "Endpoints to rate advice and retrieve ratings")
public class AdviceRatingController {
    private final AdviceRatingService adviceRatingService;

    @Operation(summary = "Rate an advice",
            description = "Submit or update a rating for a specific advice by the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rating submitted/updated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    @PostMapping("/{adviceId}")
    public ResponseEntity<AdviceRating> rateAdvice(@PathVariable Long adviceId, @RequestBody AdviceRatingDTO dto, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        AdviceRating rating = adviceRatingService.giveRating(adviceId, userId, dto.getRating());
        return ResponseEntity.ok(rating);
    }

    @Operation(summary = "Get ratings for an advice",
            description = "Retrieve all ratings given to a specific advice",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of ratings retrieved successfully")
            })
    @GetMapping("/{adviceId}")
    public ResponseEntity<List<AdviceRating>> getRatings(@PathVariable Long adviceId) {
        List<AdviceRating> ratings = adviceRatingService.getRatingsForAdvice(adviceId);
        return ResponseEntity.ok(ratings);
    }

    @Operation(summary = "Get top-rated advice",
            description = "Retrieve a list of top-rated advice entries",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of top-rated advice returned")
            })
    @GetMapping("/top-rated")
    public ResponseEntity<List<AdviceDTO>> getTopRatedAdvice(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(adviceRatingService.getTopRatedAdvice(limit));
    }

}
