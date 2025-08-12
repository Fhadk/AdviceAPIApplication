package com.descenedigital.controller;


import com.descenedigital.model.User;
import com.descenedigital.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.registerUser(user);

    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        return userService.verify(user);
    }

    @GetMapping("/users")
    public List<User> checkUsers(){
        return userService.getUsers();
    }
}