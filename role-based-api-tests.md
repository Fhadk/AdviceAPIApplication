# 🧪 Complete Role-Based API Testing Guide

## 🔑 Authentication Tokens

```bash
# ADMIN Token (admin user - has both ADMIN and USER roles)
ADMIN_TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInJvbGVzIjpbIkFETUlOIiwiVVNFUiJdLCJzdWIiOiJhZG1pbiIsImlhdCI6MTc1NDk2OTc2MiwiZXhwIjoxNzU1MDU2MTYyfQ.bjRrxcEdPitoIP8Eo19ZkudcGjOXnX9cF8_NpJIkh3I"

# USER Token (john_doe user - has only USER role)
USER_TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjIsInJvbGVzIjpbIlVTRVIiXSwic3ViIjoiam9obl9kb2UiLCJpYXQiOjE3NTQ5Njk4MDcsImV4cCI6MTc1NTA1NjIwN30.ttPwH0CJ2XSC3bPnepT1PpijrtOlQRBAOoWUi8Fu6As"
```

## 📋 Test Categories

### 1. 🌐 PUBLIC APIs (No Authentication Required)

#### Authentication Endpoints
```bash
# ✅ User Registration
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'

# ✅ User Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password"
  }'
```

#### Advice Endpoints (Read-Only)
```bash
# ✅ Get All Advice (Paginated)
curl -X GET "http://localhost:8080/api/advice?page=0&size=5&sortBy=createdAt&sortDir=desc"

# ✅ Get Advice by ID
curl -X GET http://localhost:8080/api/advice/1

# ✅ Search Advice
curl -X GET "http://localhost:8080/api/advice/search?q=productivity&page=0&size=5"

# ✅ Get Advice by Author
curl -X GET "http://localhost:8080/api/advice/author/1?page=0&size=5"

# ✅ Get Top-Rated Advice
curl -X GET "http://localhost:8080/api/advice/top-rated?page=0&size=5"

# ✅ Get Latest Advice
curl -X GET "http://localhost:8080/api/advice/latest?page=0&size=5"

# ✅ Filter Advice by Rating
curl -X GET "http://localhost:8080/api/advice/filter?minRating=4.0&page=0&size=5"
```

#### Rating Endpoints (Read-Only)
```bash
# ✅ Get Ratings for Advice
curl -X GET "http://localhost:8080/api/ratings/advice/1?page=0&size=5"

# ✅ Get Ratings by User
curl -X GET "http://localhost:8080/api/ratings/user/1?page=0&size=5"
```

### 2. 👤 USER Role APIs (Requires USER or ADMIN Role)

#### Advice Management
```bash
# ✅ Create New Advice (USER Token)
curl -X POST http://localhost:8080/api/advice \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -d '{
    "title": "Test Advice from User",
    "message": "This is a test advice created by a regular user.",
    "description": "Testing user role permissions"
  }'

# ✅ Update Own Advice (USER Token)
curl -X PUT http://localhost:8080/api/advice/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -d '{
    "title": "Updated Advice Title",
    "message": "Updated advice message",
    "description": "Updated description"
  }'

# ✅ Delete Own Advice (USER Token)
curl -X DELETE http://localhost:8080/api/advice/1 \
  -H "Authorization: Bearer $USER_TOKEN"
```

#### Rating Management
```bash
# ✅ Rate an Advice (USER Token)
curl -X POST http://localhost:8080/api/ratings/advice/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -d '{
    "rating": 5,
    "comment": "Excellent advice! Very helpful."
  }'

# ✅ Update Rating (USER Token)
curl -X PUT http://localhost:8080/api/ratings/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -d '{
    "rating": 4,
    "comment": "Good advice, updated my rating."
  }'

# ✅ Delete Rating (USER Token)
curl -X DELETE http://localhost:8080/api/ratings/1 \
  -H "Authorization: Bearer $USER_TOKEN"

# ✅ Get Current User's Ratings (USER Token)
curl -X GET "http://localhost:8080/api/ratings/my-ratings?page=0&size=5" \
  -H "Authorization: Bearer $USER_TOKEN"
```

### 3. 🔐 ADMIN Role APIs (Requires ADMIN Role Only)

#### User Management
```bash
# ✅ Get All Users (ADMIN Token)
curl -X GET "http://localhost:8080/api/users?page=0&size=10&sortBy=createdAt&sortDir=desc" \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# ✅ Get User by ID (ADMIN Token)
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# ✅ Update User Roles (ADMIN Token)
curl -X PUT http://localhost:8080/api/users/2/roles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "roles": ["USER", "ADMIN"]
  }'

# ✅ Get Users by Role (ADMIN Token)
curl -X GET "http://localhost:8080/api/users/role/USER?page=0&size=10" \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

#### Administrative Actions
```bash
# ✅ Delete Any User's Advice (ADMIN Token)
curl -X DELETE http://localhost:8080/api/advice/2 \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# ✅ Update Any User's Advice (ADMIN Token)
curl -X PUT http://localhost:8080/api/advice/3 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{
    "title": "Admin Updated Title",
    "message": "This advice was updated by admin",
    "description": "Admin can update any advice"
  }'
```

### 4. 🚫 AUTHORIZATION RESTRICTION TESTS

#### Test USER trying to access ADMIN endpoints (Should Fail)
```bash
# ❌ USER trying to access user management (Should return 403 Forbidden)
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer $USER_TOKEN"

# ❌ USER trying to get user by ID (Should return 403 Forbidden)
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer $USER_TOKEN"

# ❌ USER trying to update user roles (Should return 403 Forbidden)
curl -X PUT http://localhost:8080/api/users/1/roles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -d '{"roles": ["ADMIN"]}'
```

#### Test Unauthenticated access to protected endpoints (Should Fail)
```bash
# ❌ No token - Create Advice (Should return 401 Unauthorized)
curl -X POST http://localhost:8080/api/advice \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Unauthorized Test",
    "message": "This should fail",
    "description": "No authentication"
  }'

# ❌ No token - Rate Advice (Should return 401 Unauthorized)
curl -X POST http://localhost:8080/api/ratings/advice/1 \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 5,
    "comment": "This should fail"
  }'

# ❌ No token - Get My Ratings (Should return 401 Unauthorized)
curl -X GET http://localhost:8080/api/ratings/my-ratings
```

#### Test Invalid/Expired Token (Should Fail)
```bash
# ❌ Invalid token (Should return 401 Unauthorized)
curl -X POST http://localhost:8080/api/advice \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer invalid.token.here" \
  -d '{
    "title": "Invalid Token Test",
    "message": "This should fail",
    "description": "Invalid token"
  }'
```

## 🎯 Expected Results Summary

### ✅ Should Succeed:
- **Public endpoints**: All GET operations for advice and ratings
- **Authentication**: Login and registration
- **USER role**: CRUD operations on own advice and ratings
- **ADMIN role**: All USER operations + user management + any advice/rating management

### ❌ Should Fail:
- **USER accessing ADMIN endpoints**: 403 Forbidden
- **No authentication on protected endpoints**: 401 Unauthorized
- **Invalid/expired tokens**: 401 Unauthorized
- **Users trying to modify others' content** (unless ADMIN): 403 Forbidden


