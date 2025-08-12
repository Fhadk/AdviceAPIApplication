# 🧪 Java Developer Hiring Task: Advice API Enhancement

Welcome! This task is designed to evaluate your skills in Java Spring Boot development, API design, and secure application architecture. You’ll be working with a basic Advice API application and extending it based on your own technical judgment.

---

## 📦 Project Overview

The application should include:

- JWT-based authentication
- Role-based authorization (`ADMIN`, `USER`)
- CRUD operations for an `Advice` entity
- Paginated API responses
- H2 in-memory database
- Swagger/OpenAPI documentation

---

## 📝 Your Task

Your goal is to enhance and evolve the Advice API. You are free to make architectural, design, and implementation decisions as long as they align with best practices.

### Suggested Areas to Explore

You may choose to implement one or more of the following enhancements—or propose your own:

- **User Registration Flow**  
  Add a secure way for users to register and authenticate.

- **Advice Rating System**  
  Allow users to rate advice entries and retrieve top-rated ones.

- **Advanced Pagination or Filtering**  
  Improve the API’s usability with flexible query options.

- **Role Management**  
  Introduce role assignment or role-based access control improvements.

- **DTO Mapping and Validation**  
  Use tools like MapStruct or manual mapping to separate concerns.

- **Testing Strategy**  
  Add unit or integration tests to validate core logic.

- **Swagger Improvements**  
  Enhance API documentation with examples and descriptions.

Feel free to go beyond these suggestions if you have ideas that improve the application’s usability, scalability, or maintainability.

---

## ✅ What We’re Looking For

| Area                     | What We Value                                             |
|--------------------------|-----------------------------------------------------------|
| Code Quality             | Clean, readable, and maintainable code                   |
| Spring Boot Proficiency  | Proper use of annotations, configuration, and structure  |
| Security Awareness       | Secure handling of authentication and authorization      |
| API Design               | RESTful principles, pagination, and documentation         |
| Problem Solving          | Thoughtful decisions and creative solutions              |
| Testing (Optional)       | Demonstrated understanding of testing practices          |

---

## 🚀 Submission Instructions

- Please make sure to implement your enhancements.
- Update this README.md to explain your changes and decisions.
- Create a branch and make a pull request.

### Implementation Notes

**What was added**
- JWT auth with HS256, BCrypt passwords, stateless security and roles: ADMIN, USER  
- Registration and login endpoints with token issuance  
- Advice CRUD with ownership rules and admin override; soft delete via `isActive`  
- Rating system with unique user rating per advice and computed `averageRating`  
- Advanced pagination and filtering on `/api/advice/search` plus `/api/advice/top-rated`  
- H2 dev DB, role seeding, default admin on boot  
- Global exception handling with a consistent error envelope  
- OpenAPI docs with bearer auth and request/response examples  
- DTOs with Bean Validation and MapStruct mappers  
- Unit, WebMvc, and JPA tests for core scenarios  
- `docs/examples.http` for quick manual testing

**How to run**
- `mvn clean spring-boot:run`  
- Swagger UI: `/swagger-ui.html`  
- H2 console: `/h2-console`  
  - JDBC URL: `jdbc:h2:mem:advice-db`  
  - User: `sa`  
  - Password: `(blank)`  
- Default admin: `admin@example.com` / `Admin@1234` (override via env)

**Sample workflow**
1. `POST /api/auth/register` → get token  
2. `POST /api/advice` → create advice  
3. `POST /api/advice/{id}/rate` → rate 1..5  
4. `GET /api/advice/top-rated?page=0&size=10`

**Design decisions**
- Stateless JWT for simplicity; method-level checks for ownership and admin paths  
- Repository filtering using Specifications for extensibility  
- MapStruct keeps controllers thin and mapping consistent

**Limitations and next steps**
- Add refresh tokens with rotation and blacklist  
- Add Postgres profile and Testcontainers for CI parity  
- Add moderation workflow and richer categories
