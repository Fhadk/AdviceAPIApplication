package com.descenedigital.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



//Rating.java
@Entity
public class Rating {
 @Id @GeneratedValue
 private Long id;
 
 private int score; // 1-5
 
 @ManyToOne
 private User user;
 
 @ManyToOne
 private Advice advice;

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
 
}