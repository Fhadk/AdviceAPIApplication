package com.descenedigital.dto;

public record AdviceDto(
   Long id,
   String message,
   int ratingSum,
   int ratingCount,
   double averageRating
) {}
