package com.descenedigital.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message",nullable = false)
    private String message;

    @CreationTimestamp
    @Column(name = "created",nullable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "updated",nullable = false)
    private LocalDateTime updated;

    @Column(name = "rating_sum")
    private int ratingSum = 0;

    @Column(name = "rating_count")
    private int ratingCount = 0;
}
