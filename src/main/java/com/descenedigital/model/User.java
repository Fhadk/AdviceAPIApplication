package com.descenedigital.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false,nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

}