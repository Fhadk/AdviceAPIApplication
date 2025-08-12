package com.descenedigital.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int value; // 1..5

    @ManyToOne(fetch = FetchType.LAZY)
    private Advice advice;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
