# 👨🏻‍🏫 Lecture 09 - Spring MVC
> This repository is created as a part of assignment for Lecture 09 - Spring MVC

## ✍🏼 Assignment 03 - Employee Management and PDF Generating
### 🌳 Project Structure
```bash
lecture_9_2
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/lecture_9_2/
│   │   ├── config/
│   │   │   └── DateConfig.java
│   │   ├── controller/
│   │   │   └── EmployeeController.java
│   │   ├── data/
│   │   │   ├── employees.pdf
│   │   │   ├── sampleData.csv
│   │   │   └── template-employees.pdf
│   │   ├── model/
│   │   │   └── Employee.java
│   │   ├── repository/
│   │   │   └── EmployeeRepository.java
│   │   ├── service/
│   │   │   ├── impl/
│   │   │   │   └─ EmployeeServiceImpl.java
│   │   │   └── EmployeeService.java
│   │   ├── utils/
│   │   │   ├── DateUtils.java
│   │   │   ├── FileUtils.java
│   │   │   ├── PDFGenerator.java
│   │   │   └── ThymeleafUtils.java
│   │   └── Lecture92Application.java
│   └── resources/
│       ├── static/
│       │   ├── css
│       │   │   ├── style.css
│       │   │   └── template.css
│       │   ├── js
│       │   │   └── script.js
│       │   └── index.html
│       ├── templates/
│       │   ├── employees/
│       │   │   ├── employee-form.html
│       │   │   └── list-employees.html
│       │   └── pdf/
│       │       └── pdf-template.html
│       └── application.properties
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
├── run.bat
└── run.sh
```

### 🧩 SQL Query Data
Here is the SQL query to create the database, table, and instantiate some data.
```sql
-- Create the database
CREATE DATABASE week5_lecture9_3;

-- Use the database
USE week5_lecture9_3;

-- Create the employee table
CREATE TABLE `employee` (
    `id` VARCHAR(50) NOT NULL,
    `name` VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `dob` DATE NOT NULL,
    `address` VARCHAR(255) NOT NULL,
    `department` VARCHAR(100) NOT NULL,
    `salary` INT NOT NULL,
    PRIMARY KEY (id)
);

-- Insert dummy data into the employee table
INSERT INTO Employee (id, name, dob, address, department, salary) VALUES
('1caa1b8e-678c-41a2-9d91-234f1d75f777', 'Alice Johnson', '1985-05-15', '123 Elm Street, Springfield', 'WEB', 2000),
('2bff2b8f-789d-41b3-9e92-345f2d86f888', 'Bob Smith', '1979-12-22', '456 Oak Avenue, Springfield', 'SYSTEM', 1000),
('3ccd3c90-890e-41c4-9fa3-456f3d97f999', 'Carol Davis', '1990-08-12', '789 Pine Road, Springfield', 'MOBILE', 1500),
('4dde4d91-901f-41d5-9fb4-567f4e08faaa', 'David Wilson', '1988-07-04', '321 Maple Street, Springfield', 'QA', 1700),
('5eef5e92-0120-41e6-9fc5-678f5e19fbbb', 'Eva Brown', '1992-03-28', '654 Birch Lane, Springfield', 'ADMIN', 2500);
```

and here is the query to drop the database
```sql
-- Drop the database
DROP DATABASE IF EXISTS week5_lecture9_3;
```

Also don't forget to configure [application properties](/Week%2005/Lecture%2009/Assignment%2002/lecture_9_2/src/main/resources/application.properties) with this format
```java
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/<your_database>
spring.datasource.username=<your_user_name>
spring.datasource.password=<your_password>
```

### ⚙️ How to run the program
1. Go to the `lecture_9_2` directory by using this command
    ```bash
    $ cd lecture_9_2
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

If all the instruction is well executed, the main-view will be something like this. Open [localhost:8080](http://localhost:8080) to see the view.

### 📸 Screenshots
The main modification of the program is now the program can generate a PDF based on CSV file given from the input. Here's the documentation.

1. **Full Page**

    ![Screenshot](img/api1.png)
2. **Full Page After CSV Uploaded**

    ![Screenshot](img/api2.png)
3. **PDF Generated**

    ![Screenshot](img/api3.png)

By using [this CSV file](/Week%2005/Lecture%2009/Assignment%2003/lecture_9_2/src/main/java/com/example/lecture_9_2/data/sampleData.csv), you can see the printed template [here](/Week%2005/Lecture%2009/Assignment%2003/lecture_9_2/src/main/java/com/example/lecture_9_2/data/template-employees.pdf) and generated PDF based on the data [here](/Week%2005/Lecture%2009/Assignment%2003/lecture_9_2/src/main/java/com/example/lecture_9_2/data/employees.pdf)

### 🔥 Bonus
I try to fed my curiousness, especially on **Tyhmeleaf** which i never use before, by experimented with web styling, handling edge cases, and adding new features such as employee name search. Here's a detailed breakdown of what I've explored:

#### Styling with TailwindCSS
I used **TailwindCSS** to style my web application, which offers a wide range of design possibilities. Below are some screenshots showcasing the new look.

![Screenshot](img/api4.png)

**New UI** on main page.

#### Pagination
I implemented pagination for the employee data. This optimization ensures the backend processes only the requested data, significantly speeding up data loading.

![Screenshot](img/api5.png)

Pagination across all employee data.

#### Add Employee Page
I updated the **Add Employee Page** for a better user experience.

![Screenshot](img/api6.png)

Updated Add Employee Page.

#### Edit Employee Page
Similarly, I enhanced the **Edit Employee Page**.

![Screenshot](img/api7.png)

Updated Edit Employee Page.

#### Search Feature
I introduced a **search feature**! that allows users to search for employees by name, matching the search query partially.

![Screenshot](img/api8.png)

![Screenshot](img/api9.png)

Search Feature in action.