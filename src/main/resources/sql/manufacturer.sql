INSERT INTO manufacturers(id, name, country)
VALUES
    (1, 'Samsung', 'South Korea');

INSERT INTO manufacturers(id, name, country)
VALUES
    (2, 'LG', 'South Korea');

INSERT INTO manufacturers(id, name, country)
VALUES
    (3, 'Bosch', 'Germany');

ALTER TABLE manufacturers
    ALTER COLUMN id RESTART WITH 4;