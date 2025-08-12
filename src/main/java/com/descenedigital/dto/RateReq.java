package com.descenedigital.dto;
import jakarta.validation.constraints.*;
public record RateReq(@Min(1) @Max(5) int stars) {}
