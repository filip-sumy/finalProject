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
        '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiE7A5D5j0D4Hh4lVxJ5lA6iWz9yM5K',
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