-- Create the database
CREATE DATABASE week3_lecture6;

-- Use the database
USE week3_lecture6;

-- Create `customer` table
CREATE TABLE `customer` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(50) NOT NULL
);

-- Create `cashier` table
CREATE TABLE `cashier` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL
);

-- Create `product` table
CREATE TABLE `product` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `price` DECIMAL(10, 2) NOT NULL
);

-- Create `invoice` table
CREATE TABLE `invoice` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `customer_id` INT NOT NULL,
    `cashier_id` INT NOT NULL,
    `amount` DECIMAL(10, 2) DEFAULT 0,
    `created_date` DATETIME NOT NULL,
    
    -- Foreign keys
    FOREIGN KEY (`customer_id`) REFERENCES `customer`(`id`),
    FOREIGN KEY (`cashier_id`) REFERENCES `cashier`(`id`)
);

-- Create `invoice_detail` table
CREATE TABLE `invoice_detail` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `quantity` INT NOT NULL,
    `product_id` INT NOT NULL,
    `product_price` DECIMAL(10, 2) NOT NULL,
    `invoice_id` INT NOT NULL,
    `amount` DECIMAL(10, 2) DEFAULT 0,
    
    -- Foreign keys
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`),
    FOREIGN KEY (`invoice_id`) REFERENCES `invoice`(`id`)
);

-- Triggers for updating invoice amount based on invoice_detail
DELIMITER //

-- After Insert Trigger for invoice
CREATE TRIGGER update_invoice_amount_after_insert AFTER INSERT ON `invoice_detail`
FOR EACH ROW
BEGIN
    UPDATE `invoice`
    SET `amount` = (
        SELECT IFNULL(SUM(`amount`), 0)
        FROM `invoice_detail`
        WHERE `invoice_id` = NEW.`invoice_id`
    )
    WHERE `id` = NEW.`invoice_id`;
END //

-- After Update Trigger for invoice
CREATE TRIGGER update_invoice_amount_after_update AFTER UPDATE ON `invoice_detail`
FOR EACH ROW
BEGIN
    UPDATE `invoice`
    SET `amount` = (
        SELECT IFNULL(SUM(`amount`), 0)
        FROM `invoice_detail`
        WHERE `invoice_id` = NEW.`invoice_id`
    )
    WHERE `id` = NEW.`invoice_id`;
END //

-- After Delete Trigger for invoice
CREATE TRIGGER update_invoice_amount_after_delete AFTER DELETE ON `invoice_detail`
FOR EACH ROW
BEGIN
    UPDATE `invoice`
    SET `amount` = (
        SELECT IFNULL(SUM(`amount`), 0)
        FROM `invoice_detail`
        WHERE `invoice_id` = OLD.`invoice_id`
    )
    WHERE `id` = OLD.`invoice_id`;
END //

-- Triggers for updating invoice_detail amount based on quantity and product_price
-- Before Insert Trigger for invoice_detail
CREATE TRIGGER before_insert_invoice_detail BEFORE INSERT ON `invoice_detail`
FOR EACH ROW
BEGIN
    SET NEW.`amount` = NEW.`quantity` * NEW.`product_price`;
END //

-- Before Update Trigger for invoice_detail
CREATE TRIGGER before_update_invoice_detail BEFORE UPDATE ON `invoice_detail`
FOR EACH ROW
BEGIN
    SET NEW.`amount` = NEW.`quantity` * NEW.`product_price`;
END //

DELIMITER ;
