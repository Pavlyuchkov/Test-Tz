DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS orders_products CASCADE;

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL,
    total_amount DECIMAL(15, 2) NOT NULL,
    order_date DATE NOT NULL,
    recipient VARCHAR(100) NOT NULL,
    delivery_address VARCHAR(255) NOT NULL,
    payment_type VARCHAR(20) NOT NULL,
    delivery_type VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_details (
    id BIGSERIAL PRIMARY KEY,
    product_code BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity BIGINT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    order_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);

INSERT INTO orders (order_number, total_amount, order_date, recipient, delivery_address, payment_type, delivery_type)
VALUES
    ('ORD1001', 110000.00, '2024-03-03', 'Иван Иванов', 'Москва, ул. Ленина, д. 1', 'CARD', 'PICKUP'),
    ('ORD1002', 90000.00, '2024-04-04', 'Мария Петрова', 'Санкт-Петербург, ул. Пушкина, д. 2', 'CASH', 'DOOR_DELIVERY'),
    ('ORD1003', 29000.00, '2024-05-05', 'Сергей Смирнов', 'Екатеринбург, ул. Чехова, д. 3', 'CARD', 'PICKUP'),
    ('ORD1004', 37000.00, '2024-05-05', 'Алексей Кузнецов', 'Новосибирск, ул. Красная, д. 4', 'CASH', 'DOOR_DELIVERY'),
    ('ORD1005', 52000.00, '2024-05-05', 'Елена Васильева', 'Казань, ул. Горького, д. 5', 'CARD', 'PICKUP');

INSERT INTO order_details (product_code, product_name, quantity, unit_price, order_id)
VALUES
    (101, 'Телевизор', 1, 30000.00, 1),
    (102, 'Ноутбук', 1, 50000.00, 1),
    (103, 'Мобильный телефон', 2, 15000.00, 1),
    (101, 'Телевизор', 1, 30000.00, 2),
    (104, 'Наушники', 2, 5000.00, 2),
    (102, 'Ноутбук', 1, 50000.00, 2),
    (105, 'Кофеварка', 1, 10000.00, 3),
    (103, 'Мобильный телефон', 1, 15000.00, 3),
    (106, 'Чайник', 2, 2000.00, 3),
    (106, 'Чайник', 1, 2000.00, 4),
    (101, 'Телевизор', 1, 30000.00, 4),
    (104, 'Наушники', 1, 5000.00, 4),
    (103, 'Мобильный телефон', 2, 15000.00, 5),
    (106, 'Чайник', 1, 2000.00, 5),
    (105, 'Кофеварка', 2, 10000.00, 5);
