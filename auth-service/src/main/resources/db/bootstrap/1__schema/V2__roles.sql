DROP TABLE IF EXISTS roles;
CREATE TABLE roles
(
    id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name                       VARCHAR(255) NOT NULL,
    CONSTRAINT index_users_on_role_name UNIQUE (role_name)
);