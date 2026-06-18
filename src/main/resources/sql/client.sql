INSERT INTO users
(id,
 first_name,
 last_name,
 email,
 password,
 role)
VALUES
    (
        1,
        'John',
        'Smith',
        'john@test.com',
        '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
        'ROLE_CLIENT'
    );

INSERT INTO clients
(id,
 phone,
 address)
VALUES
    (
        1,
        '+380501112233',
        'Kyiv'
    );

ALTER TABLE users
    ALTER COLUMN id RESTART WITH 4;