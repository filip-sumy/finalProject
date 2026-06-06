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
        '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
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
        '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
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