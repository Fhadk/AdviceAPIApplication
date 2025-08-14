## Access Rules

### Authentication
- **Public**:
  - `/api/auth/login` → Any user can log in using username & password.
  - `/api/auth/public/register` → Anyone can register as a normal user (ROLE_USER). Cannot set their own role.
- **Admin**:
  - `/api/auth/register` → Admin can create new users with any role (ROLE_USER or ROLE_ADMIN).
  - Note: In this version, `/api/auth/register` has no restriction for testing purposes, but ideally only Admin should have access.

### Advice Management
- **Create Advice**: Only ADMIN can create new advice entries.
- **Update/Delete Advice**: Only ADMIN can update or delete advice.
- **View Advice**: Both USER and ADMIN can view advice paginated and toprated.
- **Rate Advice**:
  - Users can rate any advice.
  - ADMIN cannot rate their own advice.

### User Management
- **View Users List**: Only ADMIN can see the list of all users.
- **Get User by ID**: Only ADMIN can view another user's details.
- **Update Role**: Only ADMIN can change a user's role.
- **User Restrictions**: Normal users cannot create other users or assign roles.
### Swagger
- ** Added swagger implementation with proper UI
- ** Tested Swagger implementation with Jwt Authentication
### HTTP Response Codes
- ** Added http response code in every api for better testing.
- ** Tested http response code for Advice Api Application
### How to Run
- ** First create pull. 
- ** After taking pull,refresh the project in the project tool
- ** Then run,it will start on 5000 port.
- ** after run successfully go the browser,go to the "http://localhost:5000/swagger-ui/index.html"
- ** the swagger will open,where you can first register as as admin,then loggin 
- ** after logged in you will get jwt token in response.
- ** then you can perform all operation(advice,ratings) with jwt.

Here are all the testing URLs and sample payloads for Advice API:

Authentication Endpoints
1. Register Regular User
URL: POST http://localhost:5000/api/auth/register/user
Payload:
json
{
  "username": "testuser",
  "password": "password123"
}
Success Response:

json
{
  "message": "User registered successfully"
}
2. Register Admin User
URL: POST http://localhost:5000/api/auth/register/admin
Payload:

json
{
  "username": "adminuser",
  "password": "admin123"
}
Success Response:

json
{
  "message": "Admin registered successfully"
}
3. User Login
URL: POST http://localhost:5000/api/auth/login
Payload:

json
{
  "username": "testuser",
  "password": "password123"
}
Success Response:

json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "testuser",
  "roles": ["ROLE_USER"]
}
Advice Endpoints (Require JWT Token)
4. Create Advice (Admin Only)
URL: POST http://localhost:5000/api/advice
Headers:

text
Authorization: Bearer <admin_token>
Content-Type: application/json
Payload:

json
{
  "message": "This is sample advice"
}
5. Get All Advice
URL: GET http://localhost:5000/api/advice
Headers:

text
Authorization: Bearer <user_or_admin_token>
6. Get Advice by ID
URL: GET http://localhost:5000/api/advice/{id}
Headers:

text
Authorization: Bearer <user_or_admin_token>
7. Update Advice (Admin Only)
URL: PUT http://localhost:5000/api/advice/{id}
Headers:

text
Authorization: Bearer <admin_token>
Content-Type: application/json
Payload:

json
{
  "message": "Updated advice content"
}
8. Delete Advice (Admin Only)
URL: DELETE http://localhost:5000/api/advice/{id}
Headers:

text
Authorization: Bearer <admin_token>
9. Rate Advice
URL: POST http://localhost:5000/api/advice/{id}/rate
Headers:

text
Authorization: Bearer <user_token>
Content-Type: application/json
Payload:

json
{
  "score": 5
}
10. Get Top Rated Advice
URL: GET http://localhost:5000/api/advice/top-rated
Headers:

text
Authorization: Bearer <user_or_admin_token>
User Management (Admin Only)
11. Get All Users
URL: GET http://localhost:5000/api/users
Headers:

text
Authorization: Bearer <admin_token>
12. Update User Role
URL: PUT http://localhost:5000/api/users/{id}/role
Headers:

text
Authorization: Bearer <admin_token>
Content-Type: application/json
Payload:
json
{
  "role": "ROLE_ADMIN"
}
