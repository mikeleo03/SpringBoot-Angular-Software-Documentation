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

-- Create `Product` table
CREATE TABLE Product (
    ID BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    status ENUM('Active', 'Deactivate') NOT NULL,
    quantity INT(10),
    createdAt DATETIME,
    updatedAt DATETIME
);


-- Create `Invoice` table
CREATE TABLE Invoice (
    ID BINARY(16) PRIMARY KEY,
    amount INT(10) NOT NULL,
    date DATE NOT NULL,
    createdAt DATETIME,
    updatedAt DATETIME,
    customerId BINARY(16),
    FOREIGN KEY (customerId) REFERENCES Customer(ID)
);

-- Create `InvoiceDetails` table
CREATE TABLE InvoiceDetails (
    invoiceID BINARY(16),
    productID BINARY(16),
    quantity INT(10),
    productPrice INT(10),
    productName VARCHAR(255),
    amount INT(10),
    createdAt DATETIME,
    updatedAt DATETIME,
    PRIMARY KEY (invoiceID, productID),
    FOREIGN KEY (invoiceID) REFERENCES invoice(ID),
    FOREIGN KEY (productID) REFERENCES product(ID)
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

-- Insert 20 products
INSERT INTO Product (ID, name, price, status, quantity, created_at, updated_at) VALUES
(UUID_TO_BIN(UUID()), 'Product A', 100, 'Active', 50, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product B', 200, 'Active', 40, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product C', 300, 'Active', 30, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product D', 400, 'Active', 20, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product E', 500, 'Active', 10, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product F', 150, 'Active', 60, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product G', 250, 'Active', 70, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product H', 350, 'Active', 80, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product I', 450, 'Active', 90, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product J', 550, 'Active', 100, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product K', 120, 'Active', 110, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product L', 220, 'Active', 120, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product M', 320, 'Active', 130, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product N', 420, 'Active', 140, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product O', 520, 'Active', 150, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product P', 170, 'Active', 160, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product Q', 270, 'Active', 170, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product R', 370, 'Active', 180, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product S', 470, 'Active', 190, NOW(), NOW()),
(UUID_TO_BIN(UUID()), 'Product T', 570, 'Active', 200, NOW(), NOW());

-- Insert invoices and invoice details
DELIMITER //
CREATE PROCEDURE generate_invoices()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE customer_id BINARY(16);
    DECLARE product_id BINARY(16);
    DECLARE invoice_id BINARY(16);
    DECLARE rand_quantity INT;
    DECLARE rand_amount INT;
    DECLARE rand_customer_idx INT;
    DECLARE rand_product_idx INT;
    
    WHILE i < 40 DO
        SET rand_customer_idx = FLOOR(RAND() * 20);
        SET rand_product_idx = FLOOR(RAND() * 20);
        SELECT ID INTO customer_id FROM Customer LIMIT 1 OFFSET rand_customer_idx;
        SELECT ID INTO product_id FROM Product LIMIT 1 OFFSET rand_product_idx;
        
        SET invoice_id = UUID_TO_BIN(UUID());
        SET rand_quantity = FLOOR(RAND() * 10) + 1;
        SET rand_amount = (SELECT price FROM Product WHERE ID = product_id) * rand_quantity;
        
        INSERT INTO Invoice (ID, amount, date, created_at, updated_at, customer_id)
        VALUES (invoice_id, rand_amount, CURDATE(), NOW(), NOW(), customer_id);
        
        INSERT INTO Invoice_Details (invoice_ID, product_ID, quantity, price, product_name, amount, created_at, updated_at)
        VALUES (invoice_id, product_id, rand_quantity, 
                (SELECT price FROM Product WHERE ID = product_id),
                (SELECT name FROM Product WHERE ID = product_id),
                rand_amount, NOW(), NOW());
        
        SET i = i + 1;
    END WHILE;
END //
DELIMITER ;

CALL generate_invoices();