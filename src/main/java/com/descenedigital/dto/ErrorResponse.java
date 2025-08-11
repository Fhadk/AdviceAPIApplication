package com.descenedigital.dto;

public record ErrorResponse(
        int status,
        String message
) {
}