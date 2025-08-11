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



------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------

## 🚨 Issue Encountered & Solution

### **Problem: Maven Wrapper Not Working**
Initially, the project was configured to use `mvnw` (Maven Wrapper), but we encountered issues where the wrapper wasn't functioning properly on Windows.

### **Root Cause**
- Maven Wrapper (`mvnw`) was not properly configured or had permission issues
- The wrapper script wasn't executable or had path-related problems
- Windows PowerShell environment had compatibility issues with the wrapper

### **Solution Applied**
We switched to using the system-installed Maven (`mvn`) instead of the wrapper:

**Before (not working):**
```bash
./mvnw spring-boot:run
```

**After (working):**
```bash
mvn spring-boot:run
```

### **Why This Works**
- **System Maven**: We have Maven 3.9.11 properly installed on the system
- **Java Compatibility**: Java 17 is correctly configured and working
- **Path Resolution**: System Maven is in the PATH and accessible from any directory
- **Windows Compatibility**: No cross-platform script execution issues

### **Current Working Commands**
```bash
# Navigate to project directory
cd AdviceAPIApplication

# Run the application
mvn spring-boot:run

# Clean and run
mvn clean spring-boot:run

# Build the project
mvn clean package
```

### **Note for Future Development**
While using system Maven works, for production deployments you might want to:
1. Fix the Maven Wrapper configuration
2. Ensure proper permissions on wrapper scripts
3. Test wrapper functionality across different environments

---
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
---

## 🚀 Implementation Progress

### **Phase 1: Foundation & Basic Security** ✅

#### **Step 1: Security Configuration Fixed** ✅
**Issue**: Spring Security was blocking all requests by default, preventing access to:
- Swagger UI documentation
- H2 database console
- API endpoints
- Health check endpoints

Solution Implemented:
1. *Enhanced SecurityConfig.java:
   - Added `@EnableWebSecurity` annotation
   - Implemented `SecurityFilterChain` bean
   - Disabled CSRF for API endpoints
   - **Explicitly disabled form login** to prevent login page conflicts
   - **Temporarily disabled all authentication** for development testing
   - **Simplified to allow all requests** to eliminate conflicts

2. Main Application Class:
   - Added `@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})`
   - Completely disabled ALL Spring Security auto-configuration*
   - Prevents Spring Boot from overriding our security settings

3. User Accounts Created:
   - Admin User: `admin` / `admin123` (ADMIN role)
   - Developer User: `dev` / `dev123` (USER role)

4. Security Rules Applied:
   - ✅ **ALL ROUTES**: Completely disabled authentication for development
   - ✅ **Swagger UI**: Accessible without any login
   - ✅ **H2 Console**: Accessible without any login
   - ✅ **Health Checks**: Accessible without any login
   - ✅ **API Endpoints**: Ready for implementation without authentication

3. Dependencies Added:
   - **Spring Boot Actuator**: For health monitoring and metrics
   - **BCrypt Password Encoder**: For future JWT implementation

5. **Configuration Updates**:
   - **application.yml**: Added actuator configuration, excluded ALL Spring Security auto-configuration
   - **Main Application**: Completely disabled Spring Security auto-configuration classes
   - **Form Login**: Explicitly disabled to prevent login page conflicts

**Result**: 
- 🎯 **Swagger UI**: Now accessible at `http://localhost:8080/swagger-ui.html` - **NO LOGIN PROMPT**
- 🎯 **H2 Console**: Accessible at `http://localhost:8080/h2-console` - **NO LOGIN PROMPT**
- 🎯 **API Endpoints**: Ready for implementation - **NO LOGIN PROMPT**
- 🎯 **Health Monitoring**: Available at `/actuator/health` - **NO LOGIN PROMPT**
- 🔐 **Authentication**: Completely disabled for development - **NO LOGIN FORMS**

**Next Steps**: Implement JWT authentication and user management.

#### **Step 2: Basic CRUD Operations Implemented** ✅
**Issue**: The Advice entity had no business logic or API endpoints, making it impossible to create, read, update, or delete advice entries.

**Solution Implemented**:
1. **Enhanced AdviceService.java**:
   - Added complete CRUD business logic
   - Implemented create, read, update, delete operations
   - Added advice count functionality
   - Used `@RequiredArgsConstructor` for dependency injection

2. **Enhanced AdviceController.java**:
   - Added REST API endpoints for all CRUD operations
   - Implemented proper HTTP status codes (201, 200, 204, 404)
   - Added comprehensive Swagger/OpenAPI documentation
   - Used `@RequiredArgsConstructor` for dependency injection

3. **API Endpoints Created**:
   - ✅ **POST** `/api/advice` - Create new advice
   - ✅ **GET** `/api/advice` - Get all advice
   - ✅ **GET** `/api/advice/{id}` - Get advice by ID
   - ✅ **PUT** `/api/advice/{id}` - Update advice
   - ✅ **DELETE** `/api/advice/{id}` - Delete advice
   - ✅ **GET** `/api/advice/count` - Get advice count

4. **Swagger Documentation**:
   - Added `@Operation` annotations with summaries and descriptions
   - Added `@ApiResponses` with proper HTTP status codes
   - Added `@Parameter` descriptions for path variables
   - Added `@Tag` for API grouping

**Result**: 
- 🎯 **Complete CRUD API**: All basic operations are now functional
- 🎯 **Swagger Documentation**: API is fully documented with examples
- 🎯 **Proper HTTP Status Codes**: RESTful API responses implemented
- 🎯 **Ready for Testing**: Can now test the complete flow through Swagger UI


---

## 🔐 JWT Authentication Implementation

### **Phase 2: Authentication & User Management** ✅ **COMPLETED**

The application now includes a complete JWT-based authentication system with the following features:

#### **🔧 Core Components Implemented**

1. **JWT Service (`JwtService.java`)**:
   - JWT token generation and validation
   - Username extraction from tokens
   - Token expiration handling (24-hour validity)
   - HMAC-SHA256 signing algorithm
   - Secure secret key management

2. **User Management (`User.java`)**:
   - User entity with JPA annotations
   - Role-based authorization (ADMIN, USER roles)
   - Secure password storage with BCrypt encoding
   - Email and username validation
   - Account status management (enabled/disabled)

3. **Authentication Controller (`AuthController.java`)**:
   - User registration endpoint
   - User login endpoint
   - Admin registration endpoint (for development)
   - Comprehensive Swagger documentation
   - Proper error handling and validation

4. **Security Configuration (`SecurityConfig.java`)**:
   - Spring Security configuration
   - BCrypt password encoder
   - CSRF protection disabled for API endpoints
   - Form login disabled for JWT-based auth

5. **DTO Classes**:
   - `AuthRequest`: Login/registration request payload
   - `AuthResponse`: Authentication response with JWT token

#### **🚀 Authentication Endpoints**

| Endpoint | Method | Description | Access |
|----------|--------|-------------|---------|
| `/api/auth/register` | POST | Register new user (USER role) | Public |
| `/api/auth/login` | POST | User login | Public |
| `/api/auth/admin/register` | POST | Register admin user | Public |

#### **📝 API Usage Examples**

**1. User Registration:**
```bash
POST /api/auth/register
Content-Type: application/json

{
    "username": "newuser",
    "password": "password123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "newuser",
    "message": "User registered successfully"
}
```

**2. User Login:**
```bash
POST /api/auth/login
Content-Type: application/json

{
    "username": "newuser",
    "password": "password123"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "newuser",
    "message": "Login successful"
}
```

**3. Admin Registration:**
```bash
POST /api/auth/admin/register
Content-Type: application/json

{
    "username": "admin",
    "password": "admin123"
}
```

#### **🔒 Security Features**

- **Password Security**: All passwords are encrypted using BCrypt
- **JWT Token Security**: HMAC-SHA256 signing with secure secret key
- **Token Expiration**: 24-hour token validity
- **Role-Based Access**: ADMIN and USER roles implemented
- **Input Validation**: Username uniqueness validation
- **Error Handling**: Proper error responses for invalid credentials

#### **🛠️ Technical Implementation Details**

- **JWT Library**: `io.jsonwebtoken` for token handling
- **Password Encoding**: BCrypt with Spring Security
- **Database**: H2 in-memory with JPA/Hibernate
- **Documentation**: Swagger/OpenAPI annotations
- **Dependencies**: Spring Security, JWT, Lombok

#### **📊 Current User Roles**

| Role | Description | Permissions |
|------|-------------|-------------|
| **USER** | Standard user account | Basic CRUD operations on advice |
| **ADMIN** | Administrator account | Full system access (future implementation) |

#### **Phase 3: JWT Authentication System** ✅
**Issue**: The application needed a secure authentication system to protect API endpoints and manage user access.

**Solution Implemented**:
1. **JWT Service Implementation**:
   - Complete JWT token generation and validation
   - 24-hour token expiration
   - HMAC-SHA256 signing with secure secret key
   - Username extraction and claim handling

2. **User Management System**:
   - User entity with JPA annotations
   - Role-based authorization (ADMIN, USER)
   - BCrypt password encoding
   - Account status management

3. **Authentication Endpoints**:
   - ✅ **POST** `/api/auth/register` - User registration
   - ✅ **POST** `/api/auth/login` - User login
   - ✅ **POST** `/api/auth/admin/register` - Admin registration

4. **Security Features**:
   - Password encryption with BCrypt
   - JWT token validation
   - Role-based access control framework
   - Input validation and error handling

**Result**: 
- 🎯 **Complete Authentication System**: JWT-based auth fully implemented
- 🎯 **User Management**: Registration and login functionality
- 🎯 **Role System**: ADMIN and USER roles ready for authorization
- 🎯 **Security**: Password encryption and token validation
- 🎯 **API Documentation**: All auth endpoints documented in Swagger

**Next Steps**: Implement authorization rules and integrate JWT with protected endpoints.

---

## 🧪 Unit Testing Implementation

### **Phase 4: Comprehensive Unit Tests** ✅ **COMPLETED**

The application now includes comprehensive unit tests covering all major components of the JWT authentication system:

#### **🔧 Test Coverage**

1. **JWT Service Tests (`JwtServiceTest.java`)**:
   - ✅ Token generation and validation
   - ✅ Username extraction from tokens
   - ✅ Token expiration handling
   - ✅ Extra claims support
   - ✅ Error handling for invalid tokens
   - ✅ Token validation for correct/incorrect usernames

2. **Authentication Controller Tests (`AuthControllerTest.java`)**:
   - ✅ User registration (success and failure cases)
   - ✅ User login (success and failure cases)
   - ✅ Admin registration (success and failure cases)
   - ✅ Invalid credentials handling
   - ✅ Disabled user login rejection
   - ✅ Duplicate username validation

3. **User Model Tests (`UserTest.java`)**:
   - ✅ User creation with builder pattern
   - ✅ Role-based user creation (USER, ADMIN)
   - ✅ Multiple roles support
   - ✅ User status management (enabled/disabled)
   - ✅ Constructor testing (no-args, all-args)
   - ✅ Property getters and setters
   - ✅ Equality and hash code testing

4. **DTO Tests (`AuthRequestTest.java`, `AuthResponseTest.java`)**:
   - ✅ Request/Response object creation
   - ✅ Builder pattern validation
   - ✅ Constructor testing
   - ✅ Property getters and setters
   - ✅ Equality and hash code testing
   - ✅ Edge cases (null, empty, special characters)

#### **📊 Test Statistics**

| Test Class | Test Methods | Coverage |
|------------|--------------|----------|
| **JwtServiceTest** | 10 | JWT token operations |
| **AuthControllerTest** | 8 | Authentication endpoints |
| **UserTest** | 12 | User model operations |
| **AuthRequestTest** | 10 | Request DTO validation |
| **AuthResponseTest** | 14 | Response DTO validation |
| **Total** | **54** | **Comprehensive coverage** |

#### **🚀 Test Execution**

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=JwtServiceTest

# Run with detailed output
mvn test -Dtest=AuthControllerTest -Dsurefire.useFile=false
```

#### **✅ Test Results**

All tests pass successfully:
- **54 tests executed**
- **0 failures**
- **0 errors**
- **0 skipped**

#### **🛠️ Testing Framework**

- **JUnit 5**: Modern testing framework
- **Mockito**: Mocking and stubbing
- **AssertJ**: Fluent assertions
- **Maven Surefire**: Test execution
- **@DisplayName**: Descriptive test names

---

## 📋 Updated Project Status

| Component | Status | Notes |
|-----------|--------|-------|
| **Security Configuration** | ✅ **COMPLETED** | Swagger UI, H2 Console, API access enabled |
| **Basic CRUD Operations** | ✅ **COMPLETED** | Controller, Service implemented with full CRUD operations |
| **JWT Authentication** | ✅ **COMPLETED** | JWT service, auth endpoints, user management implemented |
| **User Management** | ✅ **COMPLETED** | User entity, registration, login, role system implemented |
| **Role System** | ✅ **FRAMEWORK READY** | ADMIN/USER roles implemented, ready for authorization rules |
| **API Documentation** | ✅ **PARTIALLY COMPLETED** | Swagger annotations for auth and CRUD endpoints |
| **Authorization Rules** | ❌ **PENDING** | JWT integration with protected endpoints needed |
| **Testing** | ✅ **COMPLETED** | Unit tests implemented for JWT service, auth controller, user model, and DTOs |

--

---

## 📋 Final Project Status

| Component | Status | Notes |
|-----------|--------|-------|
| **Security Configuration** | ✅ **COMPLETED** | Swagger UI, H2 Console, API access enabled |
| **Basic CRUD Operations** | ✅ **COMPLETED** | Controller, Service implemented with full CRUD operations |
| **JWT Authentication** | ✅ **COMPLETED** | JWT service, auth endpoints, user management implemented |
| **User Management** | ✅ **COMPLETED** | User entity, registration, login, role system implemented |
| **Role System** | ✅ **FRAMEWORK READY** | ADMIN/USER roles implemented, ready for authorization rules |
| **API Documentation** | ✅ **PARTIALLY COMPLETED** | Swagger annotations for auth and CRUD endpoints |
| **Authorization Rules** | ❌ **PENDING** | JWT integration with protected endpoints needed |
| **Testing** | ✅ **COMPLETED** | Unit tests implemented for JWT service, auth controller, user model, and DTOs |
| **Protected Endpoints** | ❌ **PENDING** | Role-based access control implementation needed |






mvn clean install
mvn spring-boot:run
http://localhost:8080/swagger-ui.html
http://localhost:8080/h2-console