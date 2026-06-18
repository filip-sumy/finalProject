INSERT INTO orders
(
    id,
    created_at,
    status,
    client_id
)
VALUES
    (
        1,
        CURRENT_TIMESTAMP,
        'CREATED',
        1
    );

ALTER TABLE orders
    ALTER COLUMN id RESTART WITH 2;