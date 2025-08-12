package com.descenedigital.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "advice_rating",
        uniqueConstraints = @UniqueConstraint(name = "uk_advice_user", columnNames = {"advice_id", "user_id"}),
        indexes = {
                @Index(name = "idx_rating_advice", columnList = "advice_id"),
                @Index(name = "idx_rating_user", columnList = "user_id")
        })
public class AdviceRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "advice_id", nullable = false)
    private Long adviceId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer rating; // 1..5

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public AdviceRating() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAdviceId() { return adviceId; }
    public void setAdviceId(Long adviceId) { this.adviceId = adviceId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}


