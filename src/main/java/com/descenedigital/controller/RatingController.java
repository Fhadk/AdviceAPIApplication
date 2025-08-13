package com.descenedigital.controller;

import com.descenedigital.dto.common.ApiResponse;
import com.descenedigital.dto.rating.RatingRequest;
import com.descenedigital.dto.rating.RatingResponse;
import com.descenedigital.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@Tag(name = "Ratings", description = "Rating management APIs")
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/advice/{adviceId}")
    @Operation(summary = "Rate an advice", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RatingResponse>> rateAdvice(
            @Parameter(description = "Advice ID") @PathVariable Long adviceId,
            @Valid @RequestBody RatingRequest request) {
        RatingResponse rating = ratingService.createOrUpdateRating(adviceId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Rating submitted successfully", rating));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get rating by ID")
    public ResponseEntity<ApiResponse<RatingResponse>> getRatingById(
            @Parameter(description = "Rating ID") @PathVariable Long id) {
        RatingResponse rating = ratingService.getRatingById(id);
        return ResponseEntity.ok(ApiResponse.success("Rating retrieved successfully", rating));
    }

    @GetMapping("/advice/{adviceId}")
    @Operation(summary = "Get ratings for an advice")
    public ResponseEntity<ApiResponse<Page<RatingResponse>>> getRatingsByAdvice(
            @Parameter(description = "Advice ID") @PathVariable Long adviceId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<RatingResponse> ratings = ratingService.getRatingsByAdvice(adviceId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Ratings retrieved successfully", ratings));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get ratings by user")
    public ResponseEntity<ApiResponse<Page<RatingResponse>>> getRatingsByUser(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<RatingResponse> ratings = ratingService.getRatingsByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success("User ratings retrieved successfully", ratings));
    }

    @GetMapping("/my-ratings")
    @Operation(summary = "Get current user's ratings", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<RatingResponse>>> getCurrentUserRatings(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<RatingResponse> ratings = ratingService.getCurrentUserRatings(pageable);
        return ResponseEntity.ok(ApiResponse.success("Your ratings retrieved successfully", ratings));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete rating", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRating(
            @Parameter(description = "Rating ID") @PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.ok(ApiResponse.success("Rating deleted successfully", null));
    }
}
