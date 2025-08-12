package com.descenedigital.dto;
import java.time.Instant;
public record AdviceResp(Long id, String text, String createdBy, double avgRating, int ratingsCount, Instant createdAt) {}
