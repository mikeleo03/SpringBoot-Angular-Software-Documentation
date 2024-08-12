-- Initialize table with DDLs
-- Create `Product` table
CREATE TABLE Product (
    ID VARCHAR(36) PRIMARY KEY,    -- Use VARCHAR for UUIDs
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    status VARCHAR(50) NOT NULL,   -- Use VARCHAR instead of ENUM
    quantity INT,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP
);

-- Insert 20 products
INSERT INTO Product (ID, name, price, status, quantity, created_at, updated_at) VALUES
(UUID_TO_BIN(UUID()), 'Product A', 100, 'ACTIVE', 50, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product B', 200, 'ACTIVE', 40, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product C', 300, 'ACTIVE', 30, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product D', 400, 'ACTIVE', 20, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product E', 500, 'ACTIVE', 10, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product F', 150, 'ACTIVE', 60, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product G', 250, 'ACTIVE', 70, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product H', 350, 'ACTIVE', 80, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product I', 450, 'ACTIVE', 90, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product J', 550, 'ACTIVE', 100, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product K', 120, 'ACTIVE', 110, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product L', 220, 'ACTIVE', 120, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product M', 320, 'ACTIVE', 130, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product N', 420, 'ACTIVE', 140, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product O', 520, 'ACTIVE', 150, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product P', 170, 'ACTIVE', 160, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product Q', 270, 'ACTIVE', 170, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product R', 370, 'ACTIVE', 180, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product S', 470, 'ACTIVE', 190, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product T', 570, 'ACTIVE', 200, NOW(), NOW());