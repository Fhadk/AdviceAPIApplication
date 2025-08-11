package com.descenedigital.controller;

import com.descenedigital.model.Advice;
import com.descenedigital.service.AdviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/advice")
@RequiredArgsConstructor
@Tag(name = "Advice Management", description = "APIs for managing advice entries")
public class AdviceController {

    private final AdviceService adviceService;

    /**
     * Create a new advice entry
     * POST /api/advice
     */
    @PostMapping
    @Operation(summary = "Create a new advice entry", description = "Creates a new advice entry and returns the created advice with generated ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Advice created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Advice> createAdvice(@RequestBody Advice advice) {
        Advice createdAdvice = adviceService.createAdvice(advice);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdvice);
    }

    /**
     * Get all advice entries
     * GET /api/advice
     */
    @GetMapping
    @Operation(summary = "Get all advice entries", description = "Retrieves a list of all advice entries")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all advice entries")
    })
    public ResponseEntity<List<Advice>> getAllAdvice() {
        List<Advice> adviceList = adviceService.getAllAdvice();
        return ResponseEntity.ok(adviceList);
    }

    /**
     * Get advice by ID
     * GET /api/advice/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get advice by ID", description = "Retrieves a specific advice entry by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the advice entry"),
        @ApiResponse(responseCode = "404", description = "Advice entry not found")
    })
    public ResponseEntity<Advice> getAdviceById(
            @Parameter(description = "ID of the advice entry to retrieve") @PathVariable Long id) {
        Optional<Advice> advice = adviceService.getAdviceById(id);
        return advice.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update existing advice
     * PUT /api/advice/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update advice entry", description = "Updates an existing advice entry by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Advice updated successfully"),
        @ApiResponse(responseCode = "404", description = "Advice entry not found")
    })
    public ResponseEntity<Advice> updateAdvice(
            @Parameter(description = "ID of the advice entry to update") @PathVariable Long id,
            @RequestBody Advice adviceDetails) {
        Optional<Advice> updatedAdvice = adviceService.updateAdvice(id, adviceDetails);
        return updatedAdvice.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete advice by ID
     * DELETE /api/advice/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete advice entry", description = "Deletes an advice entry by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Advice deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Advice entry not found")
    })
    public ResponseEntity<Void> deleteAdvice(
            @Parameter(description = "ID of the advice entry to delete") @PathVariable Long id) {
        boolean deleted = adviceService.deleteAdvice(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get advice count
     * GET /api/advice/count
     */
    @GetMapping("/count")
    @Operation(summary = "Get advice count", description = "Retrieves the total number of advice entries")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the count")
    })
    public ResponseEntity<Long> getAdviceCount() {
        long count = adviceService.getAdviceCount();
        return ResponseEntity.ok(count);
    }
}