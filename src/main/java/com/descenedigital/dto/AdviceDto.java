package com.descenedigital.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdviceDto(
        @Schema(description = "Unique identifier", example = "0", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        String message,

        int ratingSum,

        int ratingCount,

        @Schema(description = "Average rating", example = "")  // Empty example disables default
        double averageRating
) {}

