package com.descenedigital.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingResponse {
    
    private Long id;
    private Integer rating;
    private String comment;
    private String username;
    private Long adviceId;
    private String adviceTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
