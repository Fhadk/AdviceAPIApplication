package com.descenedigital.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateAdviceRequest {
    @NotBlank
    public String title;
    @NotBlank
    public String content;
}
