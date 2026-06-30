-- Order Capture Service Schema

CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(20) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CREATED',
    total_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    shipping_address VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS order_line_items (
    id VARCHAR(20) PRIMARY KEY,
    order_id VARCHAR(20) NOT NULL,
    item_sku VARCHAR(255) NOT NULL,
    product_name VARCHAR(255),
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(10, 2) NOT NULL,
    line_total NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_line_item_order FOREIGN KEY (order_id) REFERENCES orders(id)
);
