# 🧪 Java Developer Hiring Task: Advice API Enhancement

Hi, I’m Hamdia Nouman, a passionate Java developer with a strong foundation in backend development and a keen interest in building secure, scalable, and well-documented APIs.
With hands-on experience in Spring Boot, RESTful API design, and JWT-based security, I thrive on writing clean, maintainable code that solves real-world problems.
This project reflects my commitment to quality and best practices, and I’m excited to demonstrate my skills through this enhancement task as a step towards contributing to your company.

## What I Implemented & Why

### 1. User Registration and Authentication

* Added secure user registration (`/api/auth/register`) and login (`/api/auth/login`) with password hashing for safety.
* Implemented JWT tokens for stateless authentication, ensuring scalability and performance.
* Developed a `JwtAuthenticationFilter` that intercepts requests, validates JWT tokens, and sets authentication context, allowing secure access to protected endpoints.
* Created a default admin user at startup to manage administrative tasks from day one.
* Enforced role-based authorization, restricting sensitive operations to `ADMIN` users, improving security and control.

*Reasoning:* Secure and scalable user auth is fundamental for any modern API. JWT allows stateless sessions, reducing server load and simplifying horizontal scaling.

---

### 2. Advice CRUD Operations with Role-Based Access Control

* Built full CRUD endpoints for Advice entities.
* Restricted Create, Update, Delete operations to `ADMIN` users only, ensuring advice content is controlled and authentic.
* Allowed all authenticated users to read advice, promoting user engagement.
* Added flexible filtering by keyword and minimum rating, sorting by any field, and paginated results for usability.

*Reasoning:* By limiting modification rights, we ensure the advice content stays trustworthy. Pagination and filtering make the API consumer-friendly and scalable.

---

### 3. Advice Rating System

* Enabled authenticated users to rate advice from 1 to 5.
* Ensured one rating per user per advice; updates overwrite previous ratings to prevent rating spam or junk data.
* Provided endpoints to retrieve all ratings for an advice and fetch top-rated advice entries.
* Integrated rating filters into advice listings to highlight higher-quality advice.

*Reasoning:* Rating promotes user interaction and helps surface valuable advice. Limiting one rating per user maintains data integrity and fair rankings.

---

### 4. Swagger/OpenAPI Documentation

I have integrated Swagger into this application using the springdoc-openapi-starter-webmvc-ui dependency.
This allows me to automatically generate interactive API documentation.
* Annotated Controllers and Endpoints using @Operation and @ApiResponses from the io.swagger.v3.oas.annotations package.
  These annotations help Swagger generate detailed descriptions, request/response examples, and HTTP status codes in the UI.
* Updated Security Configuration to temporarily allow public access to Swagger endpoints (for review purposes only)
* Swagger UI URL: http://localhost:8080/swagger-ui/index.html

-- How to Access Swagger
You can run and view Swagger as follows:
* Check out my branch from the pull request:
git fetch origin pull/13/head:Hamdia-Nouman
git checkout Hamdia-Nouman

* Build and run the application:
mvn clean install
mvn spring-boot:run

* Open Swagger UI in your browser:
http://localhost:8080/swagger-ui/index.html

You will see all available endpoints, their request/response details, and you can test them directly from the browser.

Right now, Swagger is public in my application so that the reviewer can test the API without authentication.
In a real-world scenerio, I would:
*Restrict Swagger access to ADMIN.
*Or make it available only in development and test environments.

*Reasoning:* Good documentation is essential for API adoption and maintenance, reducing onboarding friction for frontend or external developers.

---

### 5. Testing Strategy

* Wrote unit tests for AuthController and AuthService, covering success and failure cases in user registration and login.
* Created integration tests simulating user login and protected endpoint access with JWT, validating end-to-end security flow.

*Reasoning:* Tests ensure critical flows work correctly and guard against regressions. Integration tests confirm real-world behavior beyond isolated units.

---

## Summary

This project demonstrates a well-rounded approach balancing **security, usability, maintainability, and quality assurance**. My enhancements reflect best practices in Spring Boot development, focusing on clean architecture, clear API design, and robust security.

---

Thank you for considering my submission. I’m excited to discuss any part of my implementation and potential improvements during the interview.


