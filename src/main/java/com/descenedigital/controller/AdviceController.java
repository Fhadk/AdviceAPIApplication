package com.descenedigital.controller;

import com.descenedigital.dto.advice.AdviceRequest;
import com.descenedigital.dto.advice.AdviceResponse;
import com.descenedigital.dto.common.ApiResponse;
import com.descenedigital.service.AdviceService;
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
@RequestMapping("/api/advice")
@RequiredArgsConstructor
@Tag(name = "Advice", description = "Advice management APIs")
public class AdviceController {

    private final AdviceService adviceService;

    @GetMapping
    @Operation(summary = "Get all advice with pagination and sorting")
    public ResponseEntity<ApiResponse<Page<AdviceResponse>>> getAllAdvice(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AdviceResponse> advice = adviceService.getAllAdvice(pageable);
        return ResponseEntity.ok(ApiResponse.success("Advice retrieved successfully", advice));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get advice by ID")
    public ResponseEntity<ApiResponse<AdviceResponse>> getAdviceById(
            @Parameter(description = "Advice ID") @PathVariable Long id) {
        AdviceResponse advice = adviceService.getAdviceById(id);
        return ResponseEntity.ok(ApiResponse.success("Advice retrieved successfully", advice));
    }

    @PostMapping
    @Operation(summary = "Create new advice", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdviceResponse>> createAdvice(
            @Valid @RequestBody AdviceRequest request) {
        AdviceResponse advice = adviceService.createAdvice(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Advice created successfully", advice));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update advice", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdviceResponse>> updateAdvice(
            @Parameter(description = "Advice ID") @PathVariable Long id,
            @Valid @RequestBody AdviceRequest request) {
        AdviceResponse advice = adviceService.updateAdvice(id, request);
        return ResponseEntity.ok(ApiResponse.success("Advice updated successfully", advice));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete advice", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAdvice(
            @Parameter(description = "Advice ID") @PathVariable Long id) {
        adviceService.deleteAdvice(id);
        return ResponseEntity.ok(ApiResponse.success("Advice deleted successfully", null));
    }

    @GetMapping("/search")
    @Operation(summary = "Search advice by title, message, or description")
    public ResponseEntity<ApiResponse<Page<AdviceResponse>>> searchAdvice(
            @Parameter(description = "Search term") @RequestParam String q,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AdviceResponse> advice = adviceService.searchAdvice(q, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", advice));
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get advice by author")
    public ResponseEntity<ApiResponse<Page<AdviceResponse>>> getAdviceByAuthor(
            @Parameter(description = "Author ID") @PathVariable Long authorId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AdviceResponse> advice = adviceService.getAdviceByAuthor(authorId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Author's advice retrieved successfully", advice));
    }

    @GetMapping("/top-rated")
    @Operation(summary = "Get top-rated advice")
    public ResponseEntity<ApiResponse<Page<AdviceResponse>>> getTopRatedAdvice(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AdviceResponse> advice = adviceService.getTopRatedAdvice(pageable);
        return ResponseEntity.ok(ApiResponse.success("Top-rated advice retrieved successfully", advice));
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest advice")
    public ResponseEntity<ApiResponse<Page<AdviceResponse>>> getLatestAdvice(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AdviceResponse> advice = adviceService.getLatestAdvice(pageable);
        return ResponseEntity.ok(ApiResponse.success("Latest advice retrieved successfully", advice));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter advice by minimum rating")
    public ResponseEntity<ApiResponse<Page<AdviceResponse>>> filterAdviceByRating(
            @Parameter(description = "Minimum rating") @RequestParam Double minRating,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("averageRating").descending());
        Page<AdviceResponse> advice = adviceService.getAdviceByMinRating(minRating, pageable);
        return ResponseEntity.ok(ApiResponse.success("Filtered advice retrieved successfully", advice));
    }
}