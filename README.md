# Advice API Enhancements

## Overview

This project enhances the Advice API with secure authentication, user interaction features, and improved API usability.

## Implemented Enhancements

### 1. User Registration & Authentication
- Secure user registration and login endpoints with JWT-based authentication.
- Passwords hashed using BCrypt for security.
- JWT tokens generated with HMAC SHA-256.
- JWT filter implemented to protect secured endpoints.

### 2. Advice Rating System
- Users can submit ratings for advice entries.
- API endpoint to add a rating to advice by ID.
- Endpoint to retrieve the top rated advice entries.

### 3. Advice Entity CRUD Operations
- Full CRUD (Create, Read, Update, Delete) support for advice entries.
- Pagination, filtering, and sorting capabilities for listing advice.

### 4. DTO Mapping and Validation
- Separation of API contract and internal model using AdviceDto.
- Custom mapper interfaces for entity-to-DTO conversion.
- Input validation with `@Valid` annotations for request payloads.

### 5. Swagger / OpenAPI Documentation
- Auto-generated AP documentation with Springdoc OpenAPI.
- Swagger UI endpoints are publicly accessible for easy API testing.
- JWT Bearer token authentication support integrated into Swagger UI.

### Note:
- JWT key is in application.yml for development and demo purposes only need to change and store it securely during production  

---

## Getting Started

### Prerequisites
- Java 17+
- Maven Wrapper (included in the project)

### Build and Run

1. **Build the project:**

```bash
 ./mvnw clean package
```
2. **Run the project:**

```bash
./mvnw spring-boot:run
```

## Optional / Future Improvements

- Adding role-based access control and role management to enhance security and authorization.  
- Extending the advice rating system with more features like average ratings and user-specific ratings.  
- Integrating MapStruct for more efficient and cleaner DTO mapping.
- Implementing email verification during registration to verify accounts.
- Use Redis or similar cache to improve API performance on frequently accessed data.
- Add unit and integration tests for critical components and endpoints.
- Provide Docker setup for easier deployment.      

---
