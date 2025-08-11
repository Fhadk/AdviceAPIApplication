package com.descenedigital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAdviceRequest {
    @NotBlank
    private String message;
}
