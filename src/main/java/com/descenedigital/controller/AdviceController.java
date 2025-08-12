package com.descenedigital.controller;

import com.descenedigital.dto.AdviceRequest;
import com.descenedigital.dto.AdviceResponse;
import com.descenedigital.dto.RatingRequest;
import com.descenedigital.service.AdviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advice")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Advice", description = "Advice management APIs")
public class AdviceController {

    private final AdviceService adviceService;

    @GetMapping
    @Operation(summary = "Get all advice with pagination and filtering")
    public ResponseEntity<Page<AdviceResponse>> getAllAdvice(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir,
            @Parameter(description = "Filter by category") @RequestParam(required = false) String category,
            @Parameter(description = "Filter by message content") @RequestParam(required = false) String message) {
        
        Page<AdviceResponse> advice = adviceService.getAllAdvice(page, size, sortBy, sortDir, category, message);
        return ResponseEntity.ok(advice);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get advice by ID")
    public ResponseEntity<AdviceResponse> getAdviceById(@PathVariable Long id) {
        AdviceResponse advice = adviceService.getAdviceById(id);
        return ResponseEntity.ok(advice);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Create new advice")
    public ResponseEntity<AdviceResponse> createAdvice(
            @Valid @RequestBody AdviceRequest request,
            Authentication authentication) {
        
        AdviceResponse advice = adviceService.createAdvice(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(advice);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @adviceService.isOwner(#id, authentication.name))")
    @Operation(summary = "Update advice (only by author or admin)")
    public ResponseEntity<AdviceResponse> updateAdvice(
            @PathVariable Long id,
            @Valid @RequestBody AdviceRequest request,
            Authentication authentication) {
        
        AdviceResponse advice = adviceService.updateAdvice(id, request, authentication.getName());
        return ResponseEntity.ok(advice);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @adviceService.isOwner(#id, authentication.name))")
    @Operation(summary = "Delete advice (only by author or admin)")
    public ResponseEntity<Void> deleteAdvice(@PathVariable Long id, Authentication authentication) {
        adviceService.deleteAdvice(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rate")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Rate an advice")
    public ResponseEntity<Void> rateAdvice(
            @PathVariable Long id,
            @Valid @RequestBody RatingRequest request,
            Authentication authentication) {
        
        adviceService.rateAdvice(id, request, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/top-rated")
    @Operation(summary = "Get top-rated advice")
    public ResponseEntity<Page<AdviceResponse>> getTopRatedAdvice(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Page<AdviceResponse> topRated = adviceService.getTopRatedAdvice(page, size);
        return ResponseEntity.ok(topRated);
    }

    @GetMapping("/my-advice")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get current user's advice")
    public ResponseEntity<Page<AdviceResponse>> getMyAdvice(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        Page<AdviceResponse> myAdvice = adviceService.getAdviceByAuthor(authentication.getName(), page, size);
        return ResponseEntity.ok(myAdvice);
    }
}