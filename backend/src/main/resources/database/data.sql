-- Provide SQL scripts here
CREATE TABLE payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(255),
    amount DECIMAL(15,2),
    currency VARCHAR(10),
    payment_method_type VARCHAR(50),
    created_at DATETIME(6)
);