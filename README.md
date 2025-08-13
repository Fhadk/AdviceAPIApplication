# 📌 Advice API – Enhanced Version

A **Spring Boot REST API** for managing advice entries with **JWT authentication, role-based authorization, Swagger documentation, and test coverage**.

---

## 🆕 Key Enhancements

### 1. **User Registration & Authentication**

* Secure **JWT-based authentication** for login and registration.
* Passwords stored using **BCrypt hashing**.
* Role-based user management (`USER` and `ADMIN` via Enum).
* Integrated **JWT filter** for securing endpoints based on user roles.
* `jwt.secret` moved to `application.properties` for **better security**.

### 2. **Role-Based Authorization**

* **Spring Security** rules:

  * `/auth/**` → Public (register/login)
  * `/api/advice/admin/**` → `ADMIN` only
  * `/api/advice/**` → `USER` or `ADMIN`
* Fine-grained access control with `@PreAuthorize`.

### 3. **Advice CRUD with Pagination & Ratings**

* Full CRUD for **Advice** entity.
* **Pagination** via `Pageable`.
* **Rating** feature for advice.
* Proper HTTP status codes:

  * `201 CREATED`
  * `200 OK`
  * `404 NOT FOUND`
  * `403 FORBIDDEN`

### 4. **Swagger / OpenAPI Documentation**

* Integrated **Springdoc OpenAPI**.
* Auto-generated API docs with test capability.
* Swagger UI:
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### 5. **DTOs & Validation**

* **DTO classes** to separate entities from API contracts.
* Validation annotations:

  * `@NotBlank`
  * `@Email`
  * `@Size`
* Improved error handling with meaningful messages.

### 6. **Security Improvements**

* `jwt.secret` stored in `application.properties` (no hardcoding).
* Disabled CSRF for stateless REST API.
* Configured **BCryptPasswordEncoder**.
* Excluded public endpoints from JWT filter.

### 7. **Testing**

* Unit tests for:

  * `AuthController` (registration & login flow)
  * `AdviceController` (CRUD, rating, pagination)
* **Mockito** for mocking.
* Verified HTTP response codes and expected responses.

---

## ✅ Summary of Deliverables

| Feature                         | Status      |
| ------------------------------- | ----------- |
| JWT Authentication              | ✅ Completed |
| Role-Based Authorization        | ✅ Completed |
| User Registration Flow          | ✅ Completed |
| Advice CRUD with Pagination     | ✅ Completed |
| Advice Rating Feature           | ✅ Completed |
| Swagger/OpenAPI Documentation   | ✅ Completed |
| Secure JWT Secret in Properties | ✅ Completed |
| HTTP Response Codes Everywhere  | ✅ Completed |
| Unit Tests                      | ✅ Completed |

---

## 🚀 How to Run

### **1. Clone the Repository**

```bash
git clone https://github.com/wajihabdullah/AdviceAPIApplication.git
cd AdviceAPIApplication
```

### **2. Build & Run**

```bash
mvn clean install
mvn spring-boot:run
```

### **3. Access the API**

```bash
Swagger UI → http://localhost:8080/swagger-ui/index.html
```

### **4. Auth Flow**

```bash
# Register
POST /auth/register

# Login (returns JWT token)
POST /auth/login
Response:
{
  "token": "your.jwt.token"
}

# Use token in Authorization header for protected endpoints
Authorization: Bearer your.jwt.token
```

### **5. Running Tests**

```bash
mvn test
```

**Tests Include:**

* User registration & login
* JWT token validation
* Advice CRUD & rating
* Role-based access verification

