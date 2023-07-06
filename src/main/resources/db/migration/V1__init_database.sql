-- Create roles table
CREATE TABLE roles
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL
);
-- Create users table
CREATE TABLE users
(
    id            UUID PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    date_created  TIMESTAMP,
    date_modified TIMESTAMP
);
-- Create admins table
CREATE TABLE admins
(
    user_id  UUID PRIMARY KEY REFERENCES users (id),
    role_id  UUID REFERENCES roles (id)

);

-- Create permissions table
CREATE TABLE permissions
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL
);


-- Create role_permissions table for the many-to-many relationship
CREATE TABLE role_permissions
(
    role_id       UUID REFERENCES roles (id),
    permission_id UUID REFERENCES permissions (id),
    PRIMARY KEY (role_id, permission_id)
);
