package com.descenedigital.repo;
import com.descenedigital.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (for authentication)
    Optional<User> findByUsername(String username);

    // Check if username exists (for registration)
    boolean existsByUsername(String username);

    // Find users by role (for admin dashboards)
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(String role);
}