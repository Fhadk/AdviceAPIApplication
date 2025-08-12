package com.descenedigital.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class RateRequest {
    @Min(1) @Max(5)
    public int value;
}
