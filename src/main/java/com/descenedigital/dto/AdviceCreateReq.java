package com.descenedigital.dto;
import jakarta.validation.constraints.*;
public record AdviceCreateReq(@NotBlank @Size(max=500) String message) {}
