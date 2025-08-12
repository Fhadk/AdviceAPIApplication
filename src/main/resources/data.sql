-- Default admin user
INSERT INTO users(id, username, password, enabled)
VALUES (
           1,
           'admin',
           '$2a$10$v8i45cdvBm6BfmXezIy0yuSvDRtwIWVObGWPNL1a0Sndb9BEVFuBO', -- bcrypt of "pass123"
           true
       );

-- Default admin role
INSERT INTO user_roles(user_id, roles)
VALUES (1, 'ADMIN');

-- Ensure next generated ID starts from 2
ALTER TABLE users ALTER COLUMN id RESTART WITH 2;
