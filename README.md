# 🧪 Java Developer Hiring Task: Advice API Enhancement

## 📦 Project Overview

This project implements an **Advice API** with:

- **JWT-based authentication** using secure HMAC-SHA256 signing.
- **Role-based authorization** for `ADMIN` and `USER` roles.
- **CRUD operations** for an `Advice` entity.
- **H2 in-memory database** for easy local setup.
- **Swagger/OpenAPI documentation** for API exploration.

---

## ✨ Enhancements Implemented

### 1. **Secure JWT Authentication**
- Implemented a secure `HS256` signing key with at least 256-bit length.
- Configured token generation with roles embedded as claims.
- Added token validation in request filters to secure API endpoints.

### 2. **Role-Based Authorization**
- `ADMIN` role: Can create, update, and delete advice entries.
- `USER` role: Can view and rate advice entries.
- Secured endpoints using `@PreAuthorize` and method-level security.

### 3. **User Registration & Login**
- Added `POST /auth/register` for secure user registration.
- Added `POST /auth/login` for JWT token generation.
- Passwords stored securely using `BCryptPasswordEncoder`.

### 4. **Advice Rating System**
- Users can rate advice entries.
- Added endpoint to fetch **top-rated advice**.

### 5. **Pagination & Filtering**
- Implemented flexible pagination using Spring Data `Pageable`.
- Added optional filtering by title or category.

### 6. **DTO Mapping & Validation**
- Introduced DTOs for requests/responses.
- Added validation annotations (e.g., `@NotBlank`, `@Size`).
- Mapped DTOs to entities using **MapStruct** for cleaner code.

### 7. **Swagger Documentation**
- Enhanced API docs with descriptions, examples, and authentication details.
- Secured Swagger UI with JWT bearer token input.
- http://localhost:8080/swagger-ui/index.html

---

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security** (JWT authentication)
- **Spring Data JPA**
- **H2 Database**
- **MapStruct**
- **Swagger / Springdoc OpenAPI**

---

## 🚀 How to Run

### 1. Clone the Repository
### 2. mvn spring-boot:run
