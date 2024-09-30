-- Create the database
CREATE DATABASE week8_lecture13;

-- Use the database
USE week8_lecture13;

-- Initialize table with DDL
-- Create `Customer` table
CREATE TABLE Customer (
    ID BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255),
    status ENUM('Active', 'Deactivate') NOT NULL,
    createdAt DATETIME,
    updatedAt DATETIME
);

-- Create `APIKey` table
CREATE TABLE APIKey (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_key VARCHAR(255) NOT NULL,
    description VARCHAR(255),         -- Description or label for the API key
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp of creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Timestamp of last update
    active BOOLEAN DEFAULT TRUE       -- Status to enable or disable the API key
);

-- Initialize data on table with DML
-- Insert 20 customers
INSERT INTO Customer (ID, name, phone_number, status, created_at, updated_at) VALUES
(UUID_TO_BIN(UUID()), 'Alice Smith', '1234567890', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Bob Johnson', '0987654321', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Charlie Brown', '1122334455', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'David Wilson', '5566778899', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Eva Davis', '2233445566', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Frank Miller', '6677889900', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Grace Lee', '9988776655', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Hank Moore', '4455667788', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Ivy Taylor', '3344556677', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Jack Anderson', '7788990011', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Kara Thomas', '9988771122', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Leo Harris', '4455662233', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Mona Clark', '5566773344', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Nate Lewis', '3344558899', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Olivia Hall', '1122336677', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Paul Young', '8899001122', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Quinn Walker', '2233447788', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Rachel Allen', '6677883344', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Sam King', '9988773344', 'Active', NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Tina Scott', '5566778899', 'Active', NOW(), NOW());

-- Prepare API Keys
INSERT INTO APIKey (api_key, description, created_at, updated_at, active) VALUES 
('12345-ABCDE', 'Primary API Key for System Access', NOW(), NOW(), TRUE),
('67890-FGHIJ', 'Secondary API Key for Testing', NOW(), NOW(), FALSE);