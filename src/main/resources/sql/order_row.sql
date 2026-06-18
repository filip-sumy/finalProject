INSERT INTO order_rows
(
    id,
    order_id,
    appliance_id,
    quantity,
    price
)
VALUES
    (
        1,
        1,
        1,
        2,
        15000
    );

ALTER TABLE order_rows
    ALTER COLUMN id RESTART WITH 2;