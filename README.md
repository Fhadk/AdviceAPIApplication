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

---

## ✅ Implemented Enhancements

- Authentication & Authorization using JWT with roles `ADMIN` and `USER`
- CRUD endpoints for `Advice` with pagination
- Validation on inputs (`@NotBlank`, max length)
- H2 in-memory DB configured; console enabled
- Swagger/OpenAPI UI available at `/swagger-ui.html`
- User registration and login endpoints
- Method-level security restricting create/update/delete to `ADMIN`
- Unit tests for service layer and controller using MockMvc

## 🔐 Auth Endpoints

- POST `/api/auth/register` body: `{ "username": "u", "password": "p", "admin": false }`
  - returns `{ token }`
- POST `/api/auth/login` body: `{ "username": "u", "password": "p" }`
  - returns `{ token }`
- Use `Authorization: Bearer <token>` header for protected routes.

## 💡 Advice Endpoints

- GET `/api/advice` with pagination params `page`, `size`, `sort`
- GET `/api/advice/{id}`
- POST `/api/advice` (ADMIN)
- PUT `/api/advice/{id}` (ADMIN)
- DELETE `/api/advice/{id}` (ADMIN)

## 🧰 Run & Test

```bash
mvn clean test
mvn spring-boot:run
```

H2 Console: `/h2-console` (JDBC URL: `jdbc:h2:mem:advice-db`, user `sa`, blank password)

Swagger UI: `/swagger-ui.html`

## 🧪 Testing Strategy

- Unit tests for `AdviceService` CRUD
- Slice tests for `AdviceController` using MockMvc + JWT
- Minimal auth flow smoke test

## 🧭 Notes on Design Decisions

- Stateless JWT auth with a short-lived token (1h)
- Roles prefixed with `ROLE_` for Spring Security
- Validation annotations to keep entity constraints simple
