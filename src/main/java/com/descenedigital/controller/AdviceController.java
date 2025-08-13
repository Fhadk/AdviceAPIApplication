package com.descenedigital.controller;

import com.descenedigital.dto.AdviceDTO;
import com.descenedigital.service.AdviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advice")
@RequiredArgsConstructor
@Tag(name = "Advice", description = "Operations related to Advice management")
public class AdviceController {
    private final AdviceService adviceService;

    @Operation(summary = "Create new advice",
            description = "Creates a new advice entry",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Advice created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @PostMapping("/create")
    public ResponseEntity<AdviceDTO> createAdvice(@Valid @RequestBody AdviceDTO dto) {
        AdviceDTO createdAdvice = adviceService.createAdvice(dto);
        return ResponseEntity.ok(createdAdvice);
    }

    @Operation(summary = "Get advice by ID",
            description = "Retrieve an advice by its ID (admin only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Advice found"),
                    @ApiResponse(responseCode = "404", description = "Advice not found"),
                    @ApiResponse(responseCode = "403", description = "Access denied")
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdviceDTO> getAdviceById(@PathVariable Long id) {
        return adviceService.getAdviceById(id)
                .map(ResponseEntity::ok)

                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get paginated advice list",
            description = "Returns a page of advice entries, optionally filtered by keyword and minimum rating",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Page of advice entries")
            })
    @GetMapping
    public ResponseEntity<Page<AdviceDTO>> getAdvicePage(@Parameter(description = "Keyword to filter advice messages") @RequestParam(required = false) String keyword,
                                                         @Parameter(description = "Minimum rating to filter advice") @RequestParam(required = false) Double minRating,
                                                         @Parameter(description = "Field to sort by") @RequestParam(defaultValue = "createdAt") String sortBy,
                                                         @Parameter(description = "Sort direction: asc or desc") @RequestParam(defaultValue = "desc") String direction,
                                                         @Parameter(description = "Page number (starting from 0)") @RequestParam(defaultValue = "0") int page,
                                                         @Parameter(description = "Page size") @RequestParam(defaultValue = "5") int size) {
        Page<AdviceDTO> result = adviceService.getAdvicePage(keyword, minRating, sortBy, direction, page, size);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Update advice by ID",
            description = "Updates an existing advice entry (admin only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Advice updated"),
                    @ApiResponse(responseCode = "404", description = "Advice not found"),
                    @ApiResponse(responseCode = "403", description = "Access denied")
            })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdviceDTO> updateAdvice(@PathVariable Long id, @Valid @RequestBody AdviceDTO dto) {
        return adviceService.updateAdvice(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete advice by ID",
            description = "Deletes an advice entry (admin only)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Advice deleted"),
                    @ApiResponse(responseCode = "404", description = "Advice not found"),
                    @ApiResponse(responseCode = "403", description = "Access denied")
            })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdvice(@PathVariable Long id) {
        if (adviceService.deleteAdvice(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}