package com.descenedigital.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//Advice.java
public class Advice {
@Id @GeneratedValue
private Long id;

private String message;

@OneToMany(mappedBy = "advice", cascade = CascadeType.ALL)
private List<Rating> ratings = new ArrayList<>();

@ManyToOne
private User user; // Track who created the advice

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

public User getUser() {
	return user;
}

public void setUser(User user) {
	this.user = user;
}


}