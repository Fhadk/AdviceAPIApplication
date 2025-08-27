package com.descenedigital.model;
import lombok.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Rating {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    public Rating() {
		
	}

	public Rating(Long id, int score, User user, Advice advice) {
		super();
		this.id = id;
		this.score = score;
		this.user = user;
		this.advice = advice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Advice getAdvice() {
		return advice;
	}

	public void setAdvice(Advice advice) {
		this.advice = advice;
	}

	@Column(nullable = false)
    private int score;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"password", "ratings"}) 
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"ratings", "user"}) 
    private Advice advice;
}