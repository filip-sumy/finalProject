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