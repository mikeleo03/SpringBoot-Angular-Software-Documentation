# 👨🏻‍🏫 Lecture 10 - SpringBoot REST API
> This repository is created as a part of assignment for Lecture 10 - SpringBoot REST API

## ✍🏼 Assignment 01 - CRUD Project for Employee Management
### 🌳 Project Structure
```bash
lecture_10
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/lecture_10/
│   │   ├── controller/
│   │   │   └── EmployeeController.java
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   └── Employee.java
│   │   │   ├── repository/
│   │   │   │   └── EmployeeRepository.java
│   │   │   └── ImportData.csv
│   │   ├── dto/
│   │   │   └── EmployeeDTO.java
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── mapper/
│   │   │   └── EmployeeMapper.java
│   │   ├── utils/
│   │   │   ├── DateUtils.java
│   │   │   └── FileUtils.java
│   │   └── Lecture10Application.java
│   └── resources/
│       └── application.properties
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
├── run.bat
└── run.sh
```

### 🧩 SQL Query Data
Here is the SQL query to create the database, table, and instantiate some data
```sql
-- Create the database
CREATE DATABASE week5_lecture10;

-- Use the database
USE week5_lecture10;

-- Create the employee table
CREATE TABLE `employee` (
    `id` VARCHAR(50) NOT NULL,
    `name` VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `dob` DATE NOT NULL,
    `address` VARCHAR(255) NOT NULL,
    `department` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

-- Insert dummy data into the employee table
INSERT INTO Employee (id, name, dob, address, department, email, phone) VALUES
('1caa1b8e-678c-41a2-9d91-234f1d75f777', 'Alice Johnson', '1985-05-15', '123 Elm Street, Springfield', 'WEB', 'alice@example.com', '+6281234567890'),
('2bff2b8f-789d-41b3-9e92-345f2d86f888', 'Bob Smith', '1979-12-22', '456 Oak Avenue, Springfield', 'SYSTEM', 'bob@example.com', '+6281234567890'),
('3ccd3c90-890e-41c4-9fa3-456f3d97f999', 'Carol Davis', '1990-08-12', '789 Pine Road, Springfield', 'MOBILE', 'carol@example.com', '+6281234567890'),
('4dde4d91-901f-41d5-9fb4-567f4e08faaa', 'David Wilson', '1988-07-04', '321 Maple Street, Springfield', 'QA', 'david@example.com', '+6281234567890'),
('5eef5e92-0120-41e6-9fc5-678f5e19fbbb', 'Eva Brown', '1992-03-28', '654 Birch Lane, Springfield', 'ADMIN', 'eva@example.com', '+6281234567890');
```

and here is the query to drop the database
```sql
-- Drop the database
DROP DATABASE IF EXISTS week5_lecture10;
```

### ⚙️ How to run the program
1. Go to the `lecture_10` directory by using this command
    ```bash
    $ cd lecture_10
    ```
2. Make sure you have maven installed on your computer, use `mvn -v` to check the version.
3. If you are using windows, you can run the program by using this command.
    ```bash
    $ ./run.bat
    ```
    And if you are using Linux, you can run the program by using this command.
    ```bash
    $ chmod +x run.sh
    $ ./run.sh
    ```

### 📸 Screenshots
Here is some result of the APIs created.
1. **Get All Employees** 
    `(GET /api/v1/employee)`

    ![Screenshot](img/api1.png)
2. **Get Employee By ID**
    `(GET /api/v1/employee/3ccd3c90-890e-41c4-9fa3-456f3d97f999)`

    ![Screenshot](img/api2.png)
3. **Add New Employee**
    `(POST /api/v1/employee)`
    
    Body (Raw):
    ```json
    {
        "name": "Michael Leon",
        "dob": "2003-12-18",
        "address": "Anytime anywhere",
        "department": "MOBILE",
        "email": "michael.leon@example.com",
        "phone": "+6281234567890"
    }
    ```

    ![Screenshot](img/api3.png)
    ![Screenshot](img/api32.png)
4. **Edit Employee**
    `(PUT /api/v1/employee/{idEmployee})`
    
    Body (Raw):
    ```json
    {
        "name": "Leon Michael",
        "dob": "2003-12-18",
        "address": "Anytime anywhere anyplace",
        "department": "MOBILE",
        "email": "leon.michael@example.com",
        "phone": "+6281234567890"
    }
    ```

    ![Screenshot](img/api4.png)
    ![Screenshot](img/api42.png)
5. **Delete Employee**
    `(DELETE /api/v1/employee/{idEmployee})`

    ![Screenshot](img/api5.png)
    ![Screenshot](img/api52.png)
6. **Get Employees by Department**
    `(GET /api/v1/employee?department=QA)`

    ![Screenshot](img/api6.png)

### 📬 Postman Collection
Here is the [postman collection](/Week%2005/Lecture%2010/Assignment%2001/Lecture%2010%20-%20Assignment%2001.postman_collection.json) you can use to demo the API functionality.