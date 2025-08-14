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
