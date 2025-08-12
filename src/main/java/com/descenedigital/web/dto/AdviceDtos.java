package com.descenedigital.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public class AdviceDtos {
    public static class CreateAdviceDTO {
        @NotBlank
        private String title;
        @NotBlank
        private String text;
        private String category;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }

    public static class UpdateAdviceDTO {
        @NotBlank
        private String title;
        @NotBlank
        private String text;
        private String category;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }

    public static class RateAdviceDTO {
        @Min(1)
        @Max(5)
        private int rating;
        public int getRating() { return rating; }
        public void setRating(int rating) { this.rating = rating; }
    }

    public static class AdviceResponseDTO {
        private Long id;
        private String title;
        private String text;
        private String category;
        private Long createdById;
        private boolean active;
        private Instant createdAt;
        private Instant updatedAt;
        private double averageRating;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public Long getCreatedById() { return createdById; }
        public void setCreatedById(Long createdById) { this.createdById = createdById; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        public Instant getCreatedAt() { return createdAt; }
        public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
        public Instant getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    }

    public static class PageEnvelope<T> {
        private T content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private String sort;

        public T getContent() { return content; }
        public void setContent(T content) { this.content = content; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
        public long getTotalElements() { return totalElements; }
        public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
        public String getSort() { return sort; }
        public void setSort(String sort) { this.sort = sort; }
    }
}


