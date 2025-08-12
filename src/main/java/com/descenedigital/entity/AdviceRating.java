package com.descenedigital.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"advice_id","user_id"}))
public class AdviceRating {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Advice advice;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private int stars;
}
