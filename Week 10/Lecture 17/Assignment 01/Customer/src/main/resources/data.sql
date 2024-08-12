-- Initialize table with DDLs
-- Create `Customer` table
CREATE TABLE Customer (
    ID VARCHAR(36) PRIMARY KEY,    -- Use VARCHAR for UUIDs
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP
);

-- Create `CustomerProduct` table
CREATE TABLE CustomerProduct (
    id VARCHAR(36) PRIMARY KEY, -- Unique identifier for each record
    customerId VARCHAR(36) NOT NULL, -- ID of the customer
    productId VARCHAR(36) NOT NULL, -- ID of the product
    quantity INT NOT NULL, -- Quantity of the product purchased
    purchasedAt TIMESTAMP, -- Timestamp of purchase
    FOREIGN KEY (customerId) REFERENCES Customer(ID)
);

-- Insert 20 customers
INSERT INTO Customer (ID, first_name, last_name, email, created_at, updated_at) VALUES
('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'John', 'Doe', 'john.doe@example.com', NOW(), NOW()),
('a2b3c4d5-e6f7-8901-bcde-f12345678901', 'Jane', 'Smith', 'jane.smith@example.com', NOW(), NOW()),
('a3b4c5d6-e7f8-9012-cdef-123456789012', 'Emily', 'Johnson', 'emily.johnson@example.com', NOW(), NOW()),
('a4b5c6d7-e8f9-0123-def0-234567890123', 'Michael', 'Brown', 'michael.brown@example.com', NOW(), NOW()),
('a5b6c7d8-e9f0-1234-ef01-345678901234', 'Sarah', 'Davis', 'sarah.davis@example.com', NOW(), NOW()),
('a6b7c8d9-f0a1-2345-f012-456789012345', 'David', 'Wilson', 'david.wilson@example.com', NOW(), NOW()),
('a7b8c9d0-0a1b-3456-0123-567890123456', 'Olivia', 'Martinez', 'olivia.martinez@example.com', NOW(), NOW()),
('a8b9c0d1-1a2b-4567-1234-678901234567', 'James', 'Anderson', 'james.anderson@example.com', NOW(), NOW()),
('a9b0c1d2-2a3b-5678-2345-789012345678', 'Sophia', 'Thomas', 'sophia.thomas@example.com', NOW(), NOW()),
('b0c1d2e3-3a4b-6789-3456-890123456789', 'Daniel', 'Taylor', 'daniel.taylor@example.com', NOW(), NOW()),
('b1c2d3e4-4a5b-7890-4567-901234567890', 'Mia', 'Harris', 'mia.harris@example.com', NOW(), NOW()),
('b2c3d4e5-5a6b-8901-5678-012345678901', 'Lucas', 'Robinson', 'lucas.robinson@example.com', NOW(), NOW()),
('b3c4d5e6-6a7b-9012-6789-123456789012', 'Charlotte', 'Lewis', 'charlotte.lewis@example.com', NOW(), NOW()),
('b4c5d6e7-7a8b-0123-7890-234567890123', 'Ethan', 'Walker', 'ethan.walker@example.com', NOW(), NOW()),
('b5c6d7e8-8a9b-1234-8901-345678901234', 'Amelia', 'Young', 'amelia.young@example.com', NOW(), NOW()),
('b6c7d8e9-9a0b-2345-9012-456789012345', 'Alexander', 'Hall', 'alexander.hall@example.com', NOW(), NOW()),
('b7c8d9e0-0a1b-3456-0123-567890123456', 'Isabella', 'Allen', 'isabella.allen@example.com', NOW(), NOW()),
('b8c9d0e1-1a2b-4567-1234-678901234567', 'Matthew', 'King', 'matthew.king@example.com', NOW(), NOW()),
('b9c0d1e2-2a3b-5678-2345-789012345678', 'Mason', 'Wright', 'mason.wright@example.com', NOW(), NOW()),
('c0d1e2f3-3a4b-6789-3456-890123456789', 'Harper', 'Scott', 'harper.scott@example.com', NOW(), NOW());

-- Insert 10 CustomerProduct
INSERT INTO customer_product (id, customer_id, product_id, quantity, purchase_date) VALUES
('e1f2g3h4-i5j6-7890-k1lm-n23456789012', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890', '11111111-1111-1111-1111-111111111111', 1, NOW()),
('e2f3g4h5-j6k7-8901-l2mn-o34567890123', 'a2b3c4d5-e6f7-8901-bcde-f12345678901', '22222222-2222-2222-2222-222222222222', 2, NOW()),
('e3f4g5h6-k7l8-9012-m3no-p45678901234', 'a3b4c5d6-e7f8-9012-cdef-123456789012', '33333333-3333-3333-3333-333333333333', 3, NOW()),
('e4f5g6h7-l8m9-0123-n4op-q56789012345', 'a4b5c6d7-e8f9-0123-def0-234567890123', '44444444-4444-4444-4444-444444444444', 4, NOW()),
('e5f6g7h8-m9n0-1234-o5pq-r67890123456', 'a5b6c7d8-e9f0-1234-ef01-345678901234', '55555555-5555-5555-5555-555555555555', 5, NOW()),
('e6f7g8h9-n0o1-2345-p6qr-s78901234567', 'a6b7c8d9-f0a1-2345-f012-456789012345', '66666666-6666-6666-6666-666666666666', 1, NOW()),
('e7f8g9h0-o1p2-3456-q7rs-t89012345678', 'a7b8c9d0-0a1b-3456-0123-567890123456', '77777777-7777-7777-7777-777777777777', 2, NOW()),
('e8f9g0h1-p2q3-4567-r8st-u90123456789', 'a8b9c0d1-1a2b-4567-1234-678901234567', '88888888-8888-8888-8888-888888888888', 3, NOW()),
('e9f0g1h2-q3r4-5678-s9tu-v01234567890', 'a9b0c1d2-2a3b-5678-2345-789012345678', '99999999-9999-9999-9999-999999999999', 4, NOW()),
('f0g1h2i3-r4s5-6789-t0uv-w12345678901', 'b0c1d2e3-3a4b-6789-3456-890123456789', '00000000-0000-0000-0000-000000000000', 5, NOW());