package com.descenedigital.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RatingDto {
    @Min(1)
    @Max(5)
    private Double rating;
}