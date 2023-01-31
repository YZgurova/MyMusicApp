USE myMusic_db;

CREATE TABLE IF NOT EXISTS credentials (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS auth_tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL,
    credentials_id INT NOT NULL,
    FOREIGN KEY (credentials_id) REFERENCES credentials(id)
);

CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS credentials_to_roles (
    credentials_id INT,
    role_id INT,
    PRIMARY KEY (credentials_id, role_id),
    FOREIGN KEY (credentials_id) REFERENCES credentials(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT INTO roles(id, name) VALUES (1, 'USER');
INSERT INTO roles(id, name) VALUES (2, 'ADMIN');
INSERT INTO roles(id, name) VALUES (3, 'CREATOR');


INSERT INTO credentials(id, username, password_hash) VALUES (1, 'admin', '$2a$10$aXkejBeA6PgfnucD4Sq5aeK6b/jAW0bcQVJXzqYqriGCCDfB4.7By');
INSERT INTO credentials_to_roles (credentials_id, role_id) VALUES (1, 2);