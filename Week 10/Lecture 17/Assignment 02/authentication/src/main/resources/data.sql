-- Initialize table with DDLs
-- Create `ApiKey` table
CREATE TABLE ApiKey (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_key VARCHAR(255) NOT NULL,
    description VARCHAR(255),         -- Description or label for the API key
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp of creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Timestamp of last update
    active BOOLEAN DEFAULT TRUE       -- Status to enable or disable the API key
);

-- Prepare API Keys
INSERT INTO api_Key (api_key, description, created_at, updated_at, active) VALUES 
('12345-ABCDE', 'Primary API Key for System Access', NOW(), NOW(), TRUE),
('67890-FGHIJ', 'Secondary API Key for Testing', NOW(), NOW(), FALSE);