package com.descenedigital.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Should create user with builder pattern")
    void shouldCreateUserWithBuilderPattern() {
        // Given & When
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .email("testuser@example.com")
                .roles(Set.of(User.Role.USER))
                .enabled(true)
                .build();

        // Then
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals(Set.of(User.Role.USER), user.getRoles());
        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("Should create admin user with admin role")
    void shouldCreateAdminUserWithAdminRole() {
        // Given & When
        User adminUser = User.builder()
                .username("admin")
                .password("adminPassword")
                .email("admin@example.com")
                .roles(Set.of(User.Role.ADMIN))
                .enabled(true)
                .build();

        // Then
        assertEquals("admin", adminUser.getUsername());
        assertEquals("adminPassword", adminUser.getPassword());
        assertEquals("admin@example.com", adminUser.getEmail());
        assertEquals(Set.of(User.Role.ADMIN), adminUser.getRoles());
        assertTrue(adminUser.isEnabled());
    }

    @Test
    @DisplayName("Should create user with multiple roles")
    void shouldCreateUserWithMultipleRoles() {
        // Given & When
        User multiRoleUser = User.builder()
                .username("multiuser")
                .password("password")
                .email("multiuser@example.com")
                .roles(Set.of(User.Role.USER, User.Role.ADMIN))
                .enabled(true)
                .build();

        // Then
        assertEquals("multiuser", multiRoleUser.getUsername());
        assertEquals(2, multiRoleUser.getRoles().size());
        assertTrue(multiRoleUser.getRoles().contains(User.Role.USER));
        assertTrue(multiRoleUser.getRoles().contains(User.Role.ADMIN));
    }

    @Test
    @DisplayName("Should create disabled user")
    void shouldCreateDisabledUser() {
        // Given & When
        User disabledUser = User.builder()
                .username("disableduser")
                .password("password")
                .email("disabled@example.com")
                .roles(Set.of(User.Role.USER))
                .enabled(false)
                .build();

        // Then
        assertEquals("disableduser", disabledUser.getUsername());
        assertFalse(disabledUser.isEnabled());
    }

    @Test
    @DisplayName("Should create user with default enabled status")
    void shouldCreateUserWithDefaultEnabledStatus() {
        // Given & When
        User user = User.builder()
                .username("defaultuser")
                .password("password")
                .email("default@example.com")
                .roles(Set.of(User.Role.USER))
                .build();

        // Then
        assertEquals("defaultuser", user.getUsername());
        assertTrue(user.isEnabled()); // Default should be true
    }

    @Test
    @DisplayName("Should create user with no-args constructor")
    void shouldCreateUserWithNoArgsConstructor() {
        // Given & When
        User user = new User();

        // Then
        assertNotNull(user);
    }

    @Test
    @DisplayName("Should create user with all-args constructor")
    void shouldCreateUserWithAllArgsConstructor() {
        // Given & When
        User user = new User(1L, "testuser", "password", "test@example.com", 
                           Set.of(User.Role.USER), true);

        // Then
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(Set.of(User.Role.USER), user.getRoles());
        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("Should set and get user properties")
    void shouldSetAndGetUserProperties() {
        // Given
        User user = new User();

        // When
        user.setId(2L);
        user.setUsername("setuser");
        user.setPassword("setpassword");
        user.setEmail("set@example.com");
        user.setRoles(Set.of(User.Role.ADMIN));
        user.setEnabled(false);

        // Then
        assertEquals(2L, user.getId());
        assertEquals("setuser", user.getUsername());
        assertEquals("setpassword", user.getPassword());
        assertEquals("set@example.com", user.getEmail());
        assertEquals(Set.of(User.Role.ADMIN), user.getRoles());
        assertFalse(user.isEnabled());
    }

    @Test
    @DisplayName("Should have correct role enum values")
    void shouldHaveCorrectRoleEnumValues() {
        // Given & When
        User.Role[] roles = User.Role.values();

        // Then
        assertEquals(2, roles.length);
        assertTrue(containsRole(roles, "ADMIN"));
        assertTrue(containsRole(roles, "USER"));
    }

    @Test
    @DisplayName("Should convert role enum to string correctly")
    void shouldConvertRoleEnumToStringCorrectly() {
        // Given & When
        String adminRole = User.Role.ADMIN.name();
        String userRole = User.Role.USER.name();

        // Then
        assertEquals("ADMIN", adminRole);
        assertEquals("USER", userRole);
    }

    @Test
    @DisplayName("Should create equal users with same properties")
    void shouldCreateEqualUsersWithSameProperties() {
        // Given
        User user1 = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .roles(Set.of(User.Role.USER))
                .enabled(true)
                .build();

        User user2 = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .roles(Set.of(User.Role.USER))
                .enabled(true)
                .build();

        // When & Then
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Should create different users with different properties")
    void shouldCreateDifferentUsersWithDifferentProperties() {
        // Given
        User user1 = User.builder()
                .id(1L)
                .username("user1")
                .password("password1")
                .email("user1@example.com")
                .roles(Set.of(User.Role.USER))
                .enabled(true)
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("user2")
                .password("password2")
                .email("user2@example.com")
                .roles(Set.of(User.Role.ADMIN))
                .enabled(false)
                .build();

        // When & Then
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    private boolean containsRole(User.Role[] roles, String roleName) {
        for (User.Role role : roles) {
            if (role.name().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}
