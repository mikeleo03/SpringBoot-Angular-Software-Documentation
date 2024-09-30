-- Initialize table with DDL
-- Create `Customer` table
CREATE TABLE Customer (
    ID VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP
);

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

-- Create `Invoice` table
CREATE TABLE Invoice (
    ID VARCHAR(36) PRIMARY KEY,
    amount INT NOT NULL,    -- Remove length specifier from INT
    date DATE NOT NULL,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP,
    customerId VARCHAR(36),
    FOREIGN KEY (customerId) REFERENCES Customer(ID)
);

-- Create `InvoiceDetails` table
CREATE TABLE InvoiceDetails (
    invoiceID VARCHAR(36),
    productID VARCHAR(36),
    quantity INT,
    productPrice INT,
    productName VARCHAR(255),
    amount INT,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP,
    PRIMARY KEY (invoiceID, productID),
    FOREIGN KEY (invoiceID) REFERENCES Invoice(ID),
    FOREIGN KEY (productID) REFERENCES Product(ID)
);
