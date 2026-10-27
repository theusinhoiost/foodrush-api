-- =========================================================================
-- V2: SEED / MOCK DATA FOR DEVELOPMENT & TESTING
-- Default password for all seeded users: 123456
-- =========================================================================

-- 1. ADDRESSES
INSERT INTO addresses (id, cep, street, number, complement, neighborhood, city, state, latitude, longitude)
VALUES
    ('a0000000-0000-0000-0000-000000000001', '01310100', 'Avenida Paulista', '1000', 'Apto 101', 'Bela Vista', 'São Paulo', 'SP', -23.564886, -46.651918),
    ('a0000000-0000-0000-0000-000000000002', '01001000', 'Praça da Sé', '100', 'Loja 1', 'Sé', 'São Paulo', 'SP', -23.550520, -46.633309),
    ('a0000000-0000-0000-0000-000000000003', '01304001', 'Rua Augusta', '1500', 'Térreo', 'Consolação', 'São Paulo', 'SP', -23.558272, -46.660246)
ON CONFLICT (id) DO NOTHING;

-- 2. USERS (Password for all: 123456)
INSERT INTO users (id, name, email, password, phone, cpf, role, active, address_id)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Matheus Cliente', 'cliente@foodrush.com', '$2a$10$OlARW.DBIbytYIddfZNZwuheibIUCnTRsNgZOpp.XUEqj8ilzjuz6', '11991112222', '52998224725', 'CLIENT', TRUE, 'a0000000-0000-0000-0000-000000000001'),
    ('22222222-2222-2222-2222-222222222222', 'Carlos Proprietário', 'restaurante@foodrush.com', '$2a$10$OlARW.DBIbytYIddfZNZwuheibIUCnTRsNgZOpp.XUEqj8ilzjuz6', '11993334444', '11144477735', 'RESTAURANT', TRUE, 'a0000000-0000-0000-0000-000000000002'),
    ('33333333-3333-3333-3333-333333333333', 'Lucas Entregador', 'entregador@foodrush.com', '$2a$10$OlARW.DBIbytYIddfZNZwuheibIUCnTRsNgZOpp.XUEqj8ilzjuz6', '11995556666', '12345678909', 'COURIER', TRUE, NULL),
    ('44444444-4444-4444-4444-444444444444', 'Admin FoodRush', 'admin@foodrush.com', '$2a$10$OlARW.DBIbytYIddfZNZwuheibIUCnTRsNgZOpp.XUEqj8ilzjuz6', '11997778888', '98765432100', 'ADMIN', TRUE, NULL)
ON CONFLICT (id) DO NOTHING;

-- 3. RESTAURANTS
INSERT INTO restaurants (id, owner_id, trade_name, cnpj, phone, base_delivery_fee, latitude, longitude, address_id, is_open)
VALUES
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1', '22222222-2222-2222-2222-222222222222', 'FoodRush Burger Artesanal', '48555407000133', '11988881111', 6.90, -23.550520, -46.633309, 'a0000000-0000-0000-0000-000000000002', TRUE),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2', '22222222-2222-2222-2222-222222222222', 'Pizzaria Bella Napoli', '11222333000181', '11988882222', 8.50, -23.558272, -46.660246, 'a0000000-0000-0000-0000-000000000003', TRUE)
ON CONFLICT (id) DO NOTHING;

-- 4. MENU ITEMS
INSERT INTO menu_items (id, restaurant_id, name, description, base_price, available)
VALUES
    ('cccccccc-cccc-cccc-cccc-ccccccccccc1', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1', 'Smash Burger Duplo', 'Dois blends bovinos de 90g, queijo cheddar derretido e pão brioche selado na manteiga', 32.90, TRUE),
    ('cccccccc-cccc-cccc-cccc-ccccccccccc2', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1', 'Batata Frita Rústica', 'Porção de batatas fritas crocantes com alecrim e páprica', 16.00, TRUE),
    ('cccccccc-cccc-cccc-cccc-ccccccccccc3', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2', 'Pizza Margherita Clássica', 'Molho de tomate San Marzano, muçarela de búfala e manjericão fresco', 54.90, TRUE)
ON CONFLICT (id) DO NOTHING;

-- 5. MENU ITEM SIZES
INSERT INTO menu_item_sizes (id, menu_item_id, name, price, stock_quantity, version, available)
VALUES
    ('dddddddd-dddd-dddd-dddd-ddddddddddd1', 'cccccccc-cccc-cccc-cccc-ccccccccccc1', 'Combo Individual (180g)', 32.90, 50, 0, TRUE),
    ('dddddddd-dddd-dddd-dddd-ddddddddddd2', 'cccccccc-cccc-cccc-cccc-ccccccccccc1', 'Monster Triplo (270g)', 41.90, 30, 0, TRUE),
    ('dddddddd-dddd-dddd-dddd-ddddddddddd3', 'cccccccc-cccc-cccc-cccc-ccccccccccc2', 'Média (250g)', 16.00, 80, 0, TRUE),
    ('dddddddd-dddd-dddd-dddd-ddddddddddd4', 'cccccccc-cccc-cccc-cccc-ccccccccccc2', 'Grande (450g)', 24.00, 40, 0, TRUE),
    ('dddddddd-dddd-dddd-dddd-ddddddddddd5', 'cccccccc-cccc-cccc-cccc-ccccccccccc3', 'Grande (8 fatias)', 54.90, 25, 0, TRUE)
ON CONFLICT (id) DO NOTHING;

-- 6. COMPLEMENT GROUPS
INSERT INTO complement_groups (id, menu_item_id, name, required, min_quantity, max_quantity)
VALUES
    ('eeeeeeee-0000-0000-0000-000000000001', 'cccccccc-cccc-cccc-cccc-ccccccccccc1', 'Adicionais do Burger', FALSE, 0, 3),
    ('eeeeeeee-0000-0000-0000-000000000002', 'cccccccc-cccc-cccc-cccc-ccccccccccc1', 'Escolha seu Molho', TRUE, 1, 2)
ON CONFLICT (id) DO NOTHING;

-- 7. COMPLEMENT ITEMS
INSERT INTO complement_items (id, group_id, name, price, available)
VALUES
    ('ffffffff-0000-0000-0000-000000000001', 'eeeeeeee-0000-0000-0000-000000000001', 'Bacon Crocante Extra', 5.00, TRUE),
    ('ffffffff-0000-0000-0000-000000000002', 'eeeeeeee-0000-0000-0000-000000000001', 'Cheddar Inglês Extra', 4.50, TRUE),
    ('ffffffff-0000-0000-0000-000000000003', 'eeeeeeee-0000-0000-0000-000000000002', 'Maionese Verde da Casa', 0.00, TRUE),
    ('ffffffff-0000-0000-0000-000000000004', 'eeeeeeee-0000-0000-0000-000000000002', 'Barbecue Defumado', 2.00, TRUE)
ON CONFLICT (id) DO NOTHING;

-- 8. ORDERS
INSERT INTO orders (id, customer_id, restaurant_id, status, subtotal, delivery_fee, total, delivery_address, idempotency_key)
VALUES
    ('99999999-9999-9999-9999-999999999991', '11111111-1111-1111-1111-111111111111', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1', 'CONFIRMED', 37.90, 6.90, 44.80, 'Avenida Paulista, 1000, Apto 101 - Bela Vista, São Paulo/SP - CEP 01310-100', 'seed-order-001')
ON CONFLICT (id) DO NOTHING;

-- 9. ORDER ITEMS
INSERT INTO order_items (id, order_id, menu_item_id, menu_item_size_id, quantity, unit_price, total_price)
VALUES
    ('88888888-8888-8888-8888-888888888881', '99999999-9999-9999-9999-999999999991', 'cccccccc-cccc-cccc-cccc-ccccccccccc1', 'dddddddd-dddd-dddd-dddd-ddddddddddd1', 1, 32.90, 37.90)
ON CONFLICT (id) DO NOTHING;

-- 10. ORDER ITEM COMPLEMENTS
INSERT INTO order_item_complements (id, order_item_id, complement_item_id, name, price, quantity)
VALUES
    ('77777777-7777-7777-7777-777777777771', '88888888-8888-8888-8888-888888888881', 'ffffffff-0000-0000-0000-000000000001', 'Bacon Crocante Extra', 5.00, 1),
    ('77777777-7777-7777-7777-777777777772', '88888888-8888-8888-8888-888888888881', 'ffffffff-0000-0000-0000-000000000003', 'Maionese Verde da Casa', 0.00, 1)
ON CONFLICT (id) DO NOTHING;

