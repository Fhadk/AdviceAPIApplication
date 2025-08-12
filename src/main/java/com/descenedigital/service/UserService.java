package com.descenedigital.service;


import com.descenedigital.model.User;
import com.descenedigital.repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class UserService {

    private final AuthenticationManager authManager;
    private final UserRepo repo;
    private final JwtService jwt;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public UserService(UserRepo repo, AuthenticationManager authManager,JwtService jwt) {
        this.repo = repo;
        this.authManager = authManager;
        this.jwt = jwt;
    }

    // Register a user by email and password
    public User registerUser(User user){

        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public List<User> getUsers(){
        return repo.findAll();
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
        if (authentication.isAuthenticated()){
            return jwt.generateToken(user.getEmail());
        }
        else{
            return "failure";
        }

    }
}
