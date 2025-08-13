package com.descenedigital.controller;

import com.descenedigital.model.Advice;
import com.descenedigital.service.AdviceService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/advice")
@Tag(name = "Advice", description = "CRUD operations for advice entries")
@SecurityRequirement(name = "bearerAuth")
public class AdviceController {

    private final AdviceService adviceService;

    public AdviceController(AdviceService adviceService) {
        this.adviceService = adviceService;
    }

    @GetMapping
    @Operation(summary = "List advice", description = "Returns paginated list of advice entries",
            responses = {@ApiResponse(responseCode = "200", description = "Page of advice",
                    content = @Content(mediaType = "application/json"))})
    public Page<Advice> list(@ParameterObject Pageable pageable) {
        return adviceService.list(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get advice by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Advice.class))),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            })
    public ResponseEntity<Advice> get(@PathVariable Long id) {
        return adviceService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create advice", description = "ADMIN only", responses = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Advice.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<Advice> create(@Valid @RequestBody Advice advice) {
        Advice created = adviceService.create(advice);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update advice", description = "ADMIN only", responses = {
            @ApiResponse(responseCode = "200", description = "Updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Advice.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<Advice> update(@PathVariable Long id, @Valid @RequestBody Advice advice) {
        return adviceService.update(id, advice)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete advice", description = "ADMIN only", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}