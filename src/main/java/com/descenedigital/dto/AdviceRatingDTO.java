package com.descenedigital.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdviceRatingDTO {
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
