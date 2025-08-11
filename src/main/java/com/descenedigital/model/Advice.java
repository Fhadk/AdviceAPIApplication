package com.descenedigital.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Double averageRating = 0.0;

    @Column(nullable = false)
    private Integer ratingCount = 0;

    private String createdBy;

    public void addRating(double rating) {
        double total = this.averageRating * this.ratingCount + rating;
        this.ratingCount++;
        this.averageRating = total / this.ratingCount;
    }
}
