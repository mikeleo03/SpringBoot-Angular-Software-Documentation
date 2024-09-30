-- 1. Creating a View: Products Customers Bought
CREATE VIEW customer_purchases AS
SELECT
    c.id AS customer_id,
    c.name AS customer_name,
    p.id AS product_id,
    p.name AS product_name,
    id.quantity,
    id.amount,
    i.created_date
FROM
    customer c
    JOIN invoice i ON c.id = i.customer_id
    JOIN invoice_detail id ON i.id = id.invoice_id
    JOIN product p ON id.product_id = p.id;

-- 2. Creating a Function: Calculate Revenue by Cashier
DELIMITER //

CREATE FUNCTION calculate_revenue_by_cashier(cashier_id INT)
RETURNS DECIMAL(10, 2)
DETERMINISTIC
BEGIN
    DECLARE total_revenue DECIMAL(10, 2);
    SELECT SUM(amount) INTO total_revenue
    FROM invoice
    WHERE cashier_id = cashier_id;
    RETURN IFNULL(total_revenue, 0);
END //

DELIMITER ;

-- 3. Creating a Table: revenue_report
CREATE TABLE `revenue_report` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `year` INT NOT NULL,
    `month` INT NOT NULL,
    `day` INT NOT NULL,
    `amount` DECIMAL(10, 2) NOT NULL
);

-- 4. Creating Stored Procedures for Revenue Calculation
DELIMITER //

-- Daily Revenue
CREATE PROCEDURE calculate_daily_revenue(IN input_day DATE)
BEGIN
    DECLARE total_revenue DECIMAL(10, 2);
    SET total_revenue = (
        SELECT IFNULL(SUM(amount), 0)
        FROM invoice
        WHERE DATE(created_date) = input_day
    );

    -- Insert or update the revenue_report
    INSERT INTO revenue_report (year, month, day, amount)
    VALUES (YEAR(input_day), MONTH(input_day), DAY(input_day), total_revenue)
    ON DUPLICATE KEY UPDATE amount = total_revenue;
END //

-- Monthly Revenue
CREATE PROCEDURE calculate_monthly_revenue(IN input_year INT, IN input_month INT)
BEGIN
    DECLARE total_revenue DECIMAL(10, 2);
    SET total_revenue = (
        SELECT IFNULL(SUM(amount), 0)
        FROM invoice
        WHERE YEAR(created_date) = input_year
          AND MONTH(created_date) = input_month
    );

    -- Insert or update the revenue_report
    INSERT INTO revenue_report (year, month, day, amount)
    VALUES (input_year, input_month, 0, total_revenue)
    ON DUPLICATE KEY UPDATE amount = total_revenue;
END //

-- Annual Revenue
CREATE PROCEDURE calculate_annual_revenue(IN input_year INT)
BEGIN
    DECLARE total_revenue DECIMAL(10, 2);
    SET total_revenue = (
        SELECT IFNULL(SUM(amount), 0)
        FROM invoice
        WHERE YEAR(created_date) = input_year
    );

    -- Insert or update the revenue_report
    INSERT INTO revenue_report (year, month, day, amount)
    VALUES (input_year, 0, 0, total_revenue)
    ON DUPLICATE KEY UPDATE amount = total_revenue;
END //

DELIMITER ;