INSERT INTO users
(id,
 first_name,
 last_name,
 email,
 password,
 role)
VALUES
    (
        2,
        'Admin',
        'Admin',
        'admin@test.com',
        '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiE7A5D5j0D4Hh4lVxJ5lA6iWz9yM5K',
        'ROLE_ADMIN'
    );

INSERT INTO employees
(id,
 position,
 salary)
VALUES
    (
        2,
        'Manager',
        50000
    );

INSERT INTO users
(id,
 first_name,
 last_name,
 email,
 password,
 role)
VALUES
    (
        3,
        'Employee',
        'Employee',
        'employee@test.com',
        '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiE7A5D5j0D4Hh4lVxJ5lA6iWz9yM5K',
        'ROLE_EMPLOYEE'
    );

INSERT INTO employees
(id,
 position,
 salary)
VALUES
    (
        3,
        'Sales Manager',
        35000
    );