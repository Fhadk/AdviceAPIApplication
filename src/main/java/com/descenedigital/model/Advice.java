package com.descenedigital.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Advice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @OneToMany(mappedBy = "advice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties({"advice", "user"}) 
    private List<Rating> ratings = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"password", "ratings", "adviceList"}) 
    private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public Advice() {
		
	}

	public Advice(Long id, String message, List<Rating> ratings, User user) {
		super();
		this.id = id;
		this.message = message;
		this.ratings = ratings;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
}