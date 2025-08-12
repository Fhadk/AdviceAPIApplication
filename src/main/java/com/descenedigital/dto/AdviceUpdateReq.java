package com.descenedigital.dto;
import jakarta.validation.constraints.*;
public record AdviceUpdateReq(@NotBlank @Size(max=500) String message) {}
