package com.descenedigital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdviceDTO {
    private long id;
    @NotBlank(message="Please leave an advice")
    private String message;
    private Double averageRating;
}
