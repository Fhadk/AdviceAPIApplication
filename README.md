# 📝 Implemented Enhancements

During the enhancement of the Advice API, I have made the following key improvements:

### 1. User Registration and Authentication
- Implemented secure user registration and login endpoints using JWT for token-based authentication.  
- Passwords are securely stored using BCrypt hashing.  
- Added role-based user management with `USER` and `ADMIN` roles using an Enum type.  
- Implemented JWT filter to secure endpoints and authorize requests based on roles.  

### 2. Role-Based Authorization
- Secured API endpoints with Spring Security to allow access only to authorized roles:  
  - `/auth/**` endpoints are public for registration and login.  
  - `/admin/**` endpoints accessible only by `ADMIN`.  
  - `/user/**` endpoints accessible only by `USER`.  
  - Other endpoints require authentication.  

### 3. Advice Entity CRUD with Pagination
- Maintained and improved CRUD operations for the Advice entity.  
- Implemented pagination support in list APIs using Spring Data’s `Pageable`.  
- Added Swagger/OpenAPI annotations to improve API documentation clarity.  

### 4. DTOs and Validation
- Introduced DTO classes to decouple internal entities from API contracts.  
- Added validation annotations (`@NotBlank`, `@Email`, etc.) to ensure input correctness and improve error handling.  

### 5. Improved Security Configuration
- Configured `SecurityFilterChain` with JWT authentication filter.  
- Disabled CSRF for simplicity in stateless API context.  
- Configured password encoder using BCrypt.  
- Excluded authentication endpoints from JWT filter.  

### Optional / Future Improvements
- Added placeholders for potential advice rating feature.  
- Prepared groundwork for role assignment management.  
- Suggested introduction of MapStruct for DTO mapping.  

---

## ✅ Summary of Deliverables

| Feature                         | Status               |
|--------------------------------|----------------------|
| JWT Authentication             | Completed            |
| Role-Based Authorization       | Completed            |
| User Registration Flow         | Completed            |
| Advice CRUD with Pagination    | Completed            |
| DTO Mapping and Validation     | Implemented          |
| Swagger/OpenAPI Documentation  | NOT DONE             |
| Testing (Unit/Integration)     | (Optional / Partial) |

---

## 🚀 How to Run

- Run the Spring Boot application.  
- Use `/auth/register` and `/auth/login` for user management.  
- Use JWT token returned from login in Authorization header (`Bearer <token>`) for protected endpoints.  

---

## Final Notes

This implementation follows Spring Boot best practices with focus on security, clean architecture, and API usability. Any questions or suggestions for improvements are welcome.
