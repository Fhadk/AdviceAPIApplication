package com.descenedigital.dto.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdviceResponse {
    
    private Long id;
    private String title;
    private String message;
    private String description;
    private String authorUsername;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double averageRating;
    private Integer ratingCount;
}
