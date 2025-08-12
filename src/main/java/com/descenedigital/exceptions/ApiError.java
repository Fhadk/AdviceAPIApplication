package com.descenedigital.exceptions;

import java.time.Instant;

public record ApiError(Instant timestamp, String path, String message) {

    public static ApiError of(String path, String message) {
        return new ApiError(Instant.now(), path, message);
    }
}
