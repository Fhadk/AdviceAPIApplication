package com.descenedigital.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "advice_ratings",uniqueConstraints = {@UniqueConstraint(columnNames = {"advice_id","user_id"})})
public class AdviceRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advice_id",nullable = false)
    private Advice advice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    //rating will be from 1 to 5
    @Column(nullable = false)
    private int rating;
}
