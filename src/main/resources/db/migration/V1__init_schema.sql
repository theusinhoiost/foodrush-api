CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =========================================================================
-- 1. ADDRESSES TABLE
-- Stores postal code (CEP), street details, and geolocation coordinates
-- =========================================================================
CREATE TABLE addresses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cep VARCHAR(8) NOT NULL,
    street VARCHAR(150) NOT NULL,
    number VARCHAR(20) NOT NULL,
    complement VARCHAR(100),
    neighborhood VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,
    latitude DECIMAL(9, 6),
    longitude DECIMAL(9, 6)
);

-- =========================================================================
-- 2. USERS TABLE
-- Stores credentials, personal identifiers (phone, optional CPF), and roles
-- =========================================================================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    cpf VARCHAR(11) UNIQUE,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    address_id UUID,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_users_address FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE SET NULL
);

-- =========================================================================
-- 3. RESTAURANTS TABLE
-- Stores restaurant profile, geolocation coordinates, and business status
-- =========================================================================
CREATE TABLE restaurants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id UUID NOT NULL,
    trade_name VARCHAR(120) NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    base_delivery_fee DECIMAL(10, 2) NOT NULL,
    latitude DECIMAL(9, 6) NOT NULL,
    longitude DECIMAL(9, 6) NOT NULL,
    address_id UUID,
    is_open BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT fk_restaurants_owner FOREIGN KEY (owner_id) REFERENCES users(id),
    CONSTRAINT fk_restaurants_address FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE SET NULL
);

-- =========================================================================
-- 3. MENU ITEMS (BASE PRODUCT / DISH)
-- Parent dish information
-- =========================================================================
CREATE TABLE menu_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    restaurant_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    base_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    available BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT fk_menu_items_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

-- =========================================================================
-- 4. MENU ITEM SIZES (PORTION VARIATIONS & INVENTORY)
-- Manages portion sizes (e.g., Small, Medium, Large) with optimistic concurrency
-- =========================================================================
CREATE TABLE menu_item_sizes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    menu_item_id UUID NOT NULL,
    name VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    version BIGINT NOT NULL DEFAULT 0,
    available BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT fk_sizes_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
);

-- =========================================================================
-- 5. COMPLEMENT GROUPS
-- Defines rules and categories for add-ons (e.g., "Sauces", "Extra Toppings")
-- =========================================================================
CREATE TABLE complement_groups (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    menu_item_id UUID NOT NULL,
    name VARCHAR(80) NOT NULL,
    required BOOLEAN DEFAULT FALSE NOT NULL,
    min_quantity INTEGER DEFAULT 0 NOT NULL,
    max_quantity INTEGER DEFAULT 1 NOT NULL,
    CONSTRAINT fk_groups_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
);

-- =========================================================================
-- 6. COMPLEMENT ITEMS (EXTRAS & ADD-ONS)
-- Specific options available under each complement group
-- =========================================================================
CREATE TABLE complement_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    group_id UUID NOT NULL,
    name VARCHAR(80) NOT NULL,
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    available BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT fk_items_group FOREIGN KEY (group_id) REFERENCES complement_groups(id) ON DELETE CASCADE
);

-- =========================================================================
-- 7. ORDERS TABLE
-- Core order entity with checkout tracking, totals, and idempotency key
-- =========================================================================
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    restaurant_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    delivery_fee DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    delivery_address TEXT NOT NULL,
    idempotency_key VARCHAR(100) UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES users(id),
    CONSTRAINT fk_orders_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

-- =========================================================================
-- 8. ORDER ITEMS TABLE
-- Line items capturing dish selection and associated size
-- =========================================================================
CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    menu_item_id UUID NOT NULL,
    menu_item_size_id UUID,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
    CONSTRAINT fk_order_items_size FOREIGN KEY (menu_item_size_id) REFERENCES menu_item_sizes(id)
);

-- =========================================================================
-- 9. ORDER ITEM COMPLEMENTS TABLE
-- Historical pricing snapshot of add-ons selected for each order item
-- =========================================================================
CREATE TABLE order_item_complements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_item_id UUID NOT NULL,
    complement_item_id UUID NOT NULL,
    name VARCHAR(80) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT fk_order_complements_order_item FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_complements_item FOREIGN KEY (complement_item_id) REFERENCES complement_items(id)
);

-- =========================================================================
-- PERFORMANCE INDEXES
-- =========================================================================
CREATE INDEX idx_orders_idempotency_key ON orders(idempotency_key);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_restaurant ON orders(restaurant_id);
CREATE INDEX idx_menu_items_restaurant ON menu_items(restaurant_id);
CREATE INDEX idx_menu_item_sizes_item ON menu_item_sizes(menu_item_id);
CREATE INDEX idx_complement_groups_item ON complement_groups(menu_item_id);
CREATE INDEX idx_complement_items_group ON complement_items(group_id);