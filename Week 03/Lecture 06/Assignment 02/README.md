# 👨🏻‍🏫 Lecture 06 - RESTful API & Relational Database
> This repository is created as a part of assignment for Lecture 06 - RESTful API & Relational Database

## 🗄️ Assignment 02 - Database Initialization and Design
### 🎯 Objectives
1. **Create the tables** according to the provided schema.
2. **Create Triggers** to maintain the rules of `invoice` and `invoice_detail`.
3. **Mock the data** following the rules.

### 🛢️ Create Tables

Here's the SQL code to create the required tables with the given fields. We’ll use `AUTO_INCREMENT` for simplicity.

```sql
-- Create the database
CREATE DATABASE week3_lecture6;

-- Use the database
USE week3_lecture6;

-- Create `customer` table
CREATE TABLE `customer` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(20)
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
```

and here is the query to drop the database
```sql
-- Drop the database
DROP DATABASE IF EXISTS week3_lecture6;
```

### 💥 Create Triggers
#### Understanding Triggers in MySQL
Triggers are special procedures in databases that automatically execute predefined actions in response to specific events on a particular table. In MySQL, triggers can be used to maintain data integrity, automate complex calculations, and enforce business rules.

In this case, triggers are used to:
- **Automatically calculate totals**: Keep the `amount` in `invoice_detail` and `invoice` tables up to date.
- **Maintain consistency**: Ensure the sum of `invoice_detail` amounts equals the `amount` in the `invoice`.

**Advantages of Using Triggers**
1. **Automation**: Automate the process of updating totals without manual intervention, reducing the chance of human error.
2. **Data Integrity**: Ensure data consistency across related tables by automatically enforcing calculations.
3. **Simplicity**: Simplify application logic by handling calculations directly in the database layer.
4. **Performance**: Reduce the need for repeated and complex queries by having the database handle updates.

#### Detailed Trigger Usage
**Triggers for `invoice` Table**

**Objective**: Keep the `amount` in the `invoice` table updated based on the sum of amounts in the `invoice_detail` table.

1. **After Insert Trigger**
    When a new `invoice_detail` row is inserted, this trigger calculates the total amount of all `invoice_detail` entries for the corresponding `invoice` and updates the `invoice.amount`.
    <br>

    ```sql
    DELIMITER //

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

    DELIMITER ;
    ```

2. **After Update Trigger**
    When a new `invoice_detail` row is updated, this trigger recalculates the total amount of all `invoice_detail` entries for the corresponding `invoice` and updates the `invoice.amount`.
    <br>

    ```sql
    DELIMITER //

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

    DELIMITER ;
    ```

3. **After Delete Trigger**
    When a new `invoice_detail` row is deleted, this trigger recalculates the total amount of all remaining `invoice_detail` entries for the corresponding `invoice` and updates the `invoice.amount`.
    <br>

    ```sql
    DELIMITER //

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

    DELIMITER ;
    ```
<br>

**Triggers for `invoice_detail` Table**

**Objective**: Automatically calculate the `amount` for each `invoice_detail` row as `quantity * product_price`.

1. **Before Insert Trigger**
    Before inserting a new `invoice_detail` row, this trigger sets the `amount` to `quantity * product_price`.
    <br>

    ```sql
    DELIMITER //

    CREATE TRIGGER before_insert_invoice_detail BEFORE INSERT ON `invoice_detail`
    FOR EACH ROW
    BEGIN
        SET NEW.`amount` = NEW.`quantity` * NEW.`product_price`;
    END //

    DELIMITER ;
    ```

2. **Before Update Trigger**
    Before updating an `invoice_detail` row, this trigger recalculates and sets the `amount` based on the updated `quantity` and `product_price`.
    <br>

    ```sql
    DELIMITER //

    CREATE TRIGGER before_update_invoice_detail BEFORE UPDATE ON `invoice_detail`
    FOR EACH ROW
    BEGIN
        SET NEW.`amount` = NEW.`quantity` * NEW.`product_price`;
    END //

    DELIMITER ;
    ```

Those are all the Data Definition Languange (DDL) queries for the data. Full detail on all of them is available on [this sql file](/Week%2003/Lecture%2006/Assignment%2002/ddl_data.sql)


### 🤔 Create Mock Data

In order to mock the data with dummy data, i already created a java file [here](/Week%2003/Lecture%2006/Assignment%2002/SQLFileWriter.java).

Just run the program and the randomozed dummy data will be created in [this sql file](/Week%2003/Lecture%2006/Assignment%2002/mock_data.sql).

### 📝 Table Description

- **Table Structure**
  - `customer`: Stores customer information.
  - `cashier`: Stores cashier information.
  - `product`: Stores product information.
  - `invoice`: Represents an invoice; contains references to `customer` and `cashier`, and has a calculated `amount`.
  - `invoice_detail`: Stores the details of each invoice line; contains references to `product` and `invoice`, and has a calculated `amount`.

- **Mock Data**
    
    Inserting records into each table to provide examples of data.

- **Invoice Calculation**
  The amount in `invoice` is calculated as the sum of `amount` in `invoice_detail`.