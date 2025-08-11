package com.descenedigital.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class AdviceDto {
    private Long id;
    private String message;
    private Double averageRating;
    private Integer ratingCount;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}
