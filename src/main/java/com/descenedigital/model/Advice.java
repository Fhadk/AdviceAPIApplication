package com.descenedigital.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "advices")
public class Advice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String message;
    private Double averageRating;
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt=LocalDateTime.now();
}
