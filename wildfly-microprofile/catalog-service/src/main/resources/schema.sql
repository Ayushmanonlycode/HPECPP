-- Catalog Service Schema

CREATE TABLE IF NOT EXISTS categories (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    species VARCHAR(255),
    category_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS items (
    id VARCHAR(255) PRIMARY KEY,
    sku VARCHAR(255) NOT NULL UNIQUE,
    list_price NUMERIC(10, 2) NOT NULL,
    description VARCHAR(500),
    image_url VARCHAR(500),
    product_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);
