# 👩🏻‍🏫 Lecture 12 - Spring Data JPA
> This repository is created as a part of assignment for Lecture 12 - Spring Data JPA

## ⚡ Assignment 01 - Adding Dynamic Criteria for Employee Search

### 🔎 Dynamic Search Criteria 😵😵

To implement dynamic criteria search APIs for every attribute on my `Employee` model, i'll need to enhance my existing codebase to support filtering based on various attributes. Here's a detailed approach:

### 👣 Step-by-Step Explanation

1. **Define Search Criteria**: I decided how i want to pass search criteria to my API. Common approaches include query parameters (`/api/v1/employees?firstName=John&gender=M`) or a JSON object in the request body (`POST` request with a JSON body containing search criteria). In this implementation, i choose the query parameters.

2. **DTO (Data Transfer Object)**: I used DTOs to transfer data between layers (controller, service, repository). This helps in decoupling my API contract from my entity structure and provides flexibility in handling incoming requests.

3. **Service Layer Modification**: I enhanced my service layer to handle dynamic filtering using specifications or query methods. Specifications are particularly useful for complex queries involving multiple criteria.

4. **Controller Layer Modification**: I also modified my controller to accept dynamic search criteria and delegate the search to the service layer.

5. **Implementation Considerations**: I also not forget to handle various scenarios such as no search criteria provided, pagination, sorting, and proper error handling for invalid queries.

### 👨🏻‍💻 Implementation:

#### 1. Create a DTO for Search Criteria ([EmployeeSearchCriteriaDTO.java](/Week%2006/Lecture%2012/Assignment%2001/lecture_12/src/main/java/com/example/lecture_12/dto/EmployeeSearchCriteriaDTO.java))

```java
package com.example.lecture_12.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class EmployeeSearchCriteriaDTO {
    private LocalDate birthDate;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate hireDate;
}
```

#### 2. Update Employee Repository ([EmployeeRepository.java](/Week%2006/Lecture%2012/Assignment%2001/lecture_12/src/main/java/com/example/lecture_12/data/repository/EmployeeRepository.java))

```java
package com.example.lecture_12.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lecture_12.data.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // Define a custom query method using Specification and Pageable
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);
}
```

#### 3. Modify Employee Service Interface ([EmployeeService.java](/Week%2006/Lecture%2012/Assignment%2001/lecture_12/src/main/java/com/example/lecture_12/services/EmployeeService.java))

```java
package com.example.lecture_12.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.lecture_12.data.model.Employee;
import com.example.lecture_12.dto.EmployeeSearchCriteriaDTO;

public interface EmployeeService {
    ....

    // Retrieves a paginated list of {@link Employee} entities based on the provided search criteria.
    Page<Employee> findByCriteria(EmployeeSearchCriteriaDTO criteria, Pageable pageable);

    ....
}
```

#### 4. Implement Employee Service ([EmployeeServiceImpl.java](/Week%2006/Lecture%2012/Assignment%2001/lecture_12/src/main/java/com/example/lecture_12/services/impl/EmployeeServiceImpl.java))

```java
package com.example.lecture_12.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.lecture_12.data.model.Employee;
import com.example.lecture_12.data.repository.EmployeeRepository;
import com.example.lecture_12.dto.EmployeeSearchCriteriaDTO;
import com.example.lecture_12.services.EmployeeService;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;

    ....

    /**
     * Retrieves a paginated list of {@link Employee} entities based on the provided search criteria.
     *
     * @param criteria The criteria object containing fields to filter the search.
     * @param pageable Pagination and sorting parameters.
     * @return A {@link Page} of {@link Employee} entities that match the specified criteria.
     */
    @Override
    public Page<Employee> findByCriteria(EmployeeSearchCriteriaDTO criteria, Pageable pageable) {
        return employeeRepository.findAll((Specification<Employee>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (criteria.getBirthDate() != null) { predicates.add(cb.equal(root.get("birthDate"), criteria.getBirthDate())); }
            if (criteria.getFirstName() != null) { predicates.add(cb.equal(root.get("firstName"), criteria.getFirstName())); }
            if (criteria.getLastName() != null) { predicates.add(cb.equal(root.get("lastName"), criteria.getLastName())); }
            if (criteria.getGender() != null) { predicates.add(cb.equal(root.get("gender"), criteria.getGender())); }
            if (criteria.getHireDate() != null) { predicates.add(cb.equal(root.get("hireDate"), criteria.getHireDate())); }

            return cb.and(predicates.toArray(Predicate[]::new));
        }, pageable);
    }

    ....
}
```

#### 5. Update Employee Controller ([EmployeeController.java](/Week%2006/Lecture%2012/Assignment%2001/lecture_12/src/main/java/com/example/lecture_12/controllers/EmployeeController.java))

```java
package com.example.lecture_12.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.lecture_12.data.model.Employee;
import com.example.lecture_12.dto.EmployeeSearchCriteriaDTO;
import com.example.lecture_12.services.EmployeeService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    ....

    /**
     * Endpoint to search for {@link Employee} entities based on the provided search criteria.
     * Supports pagination and sorting.
     *
     * @param criteria The criteria object of {@link EmployeeSearchCriteriaDTO} containing fields to filter the search.
     * @param page     The page number to retrieve (default is 0).
     * @param size     The number of elements per page (default is 20).
     * @return ResponseEntity containing a {@link Page} of {@link Employee} entities that match the criteria,  
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Employee>> searchEmployees(EmployeeSearchCriteriaDTO criteria, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeService.findByCriteria(criteria, pageable);

        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(employees);
    }

    ....
}
```

### 📢 Explanation of Code
Here is the detail explanation on what i already done throughout the code.

- **DTO**: `EmployeeSearchCriteriaDTO` is a simple class to hold search criteria. Each attribute corresponds to a field in the `Employee` entity.
- **Database Layer (JPA)**: adding `findAll` with Specification and Pageable on `EmployeeRepository` to handle spesific search query criteria dynamically from the database also to implement pagination easily.
- **Service Layer**: `EmployeeServiceImpl` implements `findByCriteria` method using JPA Specifications to dynamically build predicates based on provided criteria.
- **Controller Layer**: `EmployeeController` exposes a `GET` endpoint `/api/v1/employees/search` to accept search criteria as query parameters and returns a list of matching `Employee` entities.

### 📝 Some Notable Mentions

- **Security**: I'm ensuring to validate and sanitize input to prevent injection attacks.
- **Performance**: Instead of just showing all the filtered criteria, i also use pagination (`Pageable`) to handle large result sets efficiently.
- **Flexibility**: In the program i implemented, i expand the approach by handling more complex queries using JPA `Specifications` which makes the execution more smooth and dynamic.

This approach ensures my API to be flexible, maintainable, and follows best practices for handling dynamic search criteria in a Spring Boot application using JPA.

---

### 🌳 Project Structure
```bash
lecture_11
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/lecture_11/
│   │   ├── controller/
│   │   │   ├── DepartmentController.java
│   │   │   ├── EmployeeController.java
│   │   │   ├── SalaryController.java
│   │   │   └── TitleController.java
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   ├── composite/
│   │   │   │   │   ├── DeptEmpId.java
│   │   │   │   │   ├── DeptManagerId.java
│   │   │   │   │   ├── SalaryId.java
│   │   │   │   │   └── TitleId.java
│   │   │   │   ├── Department.java
│   │   │   │   ├── DeptEmp.java
│   │   │   │   ├── DeptManager.java
│   │   │   │   ├── Employee.java
│   │   │   │   ├── Salary.java
│   │   │   │   └── Title.java
│   │   │   └── repository/
│   │   │       ├── DepartmentRepository.java
│   │   │       ├── DeptEmpRepository.java
│   │   │       ├── DeptManagerRepository.java
│   │   │       ├── EmployeeRepository.java
│   │   │       ├── Salary.Repositoryjava
│   │   │       └── TitleRepository.java
│   │   ├── dto/
│   │   │   └── EmployeeSearchCriteriaDTO.java
│   │   ├── service/
│   │   │   ├── impl/
│   │   │   │   ├── DepartmentServiceImpl.java
│   │   │   │   ├── EmployeeServiceImpl.java
│   │   │   │   ├── SalaryServiceImpl.java
│   │   │   │   └── TitleServiceImpl.java
│   │   │   ├── DepartmentService.java
│   │   │   ├── EmployeeService.java
│   │   │   ├── SalaryService.java
│   │   │   └── TitleService.java
│   │   └── Lecture11Application.java
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
Here is the SQL query to create the database, table, and instantiate some data.
```sql
-- Create the database
CREATE DATABASE week6_lecture12;

-- Use the database
USE week6_lecture12;

-- Create employees table
CREATE TABLE employees (
    emp_no INT AUTO_INCREMENT PRIMARY KEY,
    birth_date DATE NOT NULL,
    first_name VARCHAR(14) NOT NULL,
    last_name VARCHAR(16) NOT NULL,
    gender ENUM('M', 'F') NOT NULL,
    hire_date DATE NOT NULL
);

-- Create departments table
CREATE TABLE departments (
    dept_no CHAR(4) PRIMARY KEY,
    dept_name VARCHAR(40) NOT NULL UNIQUE
);

-- Create dept_emp table
CREATE TABLE dept_emp (
    emp_no INT NOT NULL,
    dept_no CHAR(4) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    PRIMARY KEY (emp_no, dept_no),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no) ON DELETE CASCADE,
    FOREIGN KEY (dept_no) REFERENCES departments(dept_no) ON DELETE CASCADE
);

-- Create dept_manager table
CREATE TABLE dept_manager (
    emp_no INT NOT NULL,
    dept_no CHAR(4) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,
    PRIMARY KEY (emp_no, dept_no),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no) ON DELETE CASCADE,
    FOREIGN KEY (dept_no) REFERENCES departments(dept_no) ON DELETE CASCADE
);

-- Create salaries table
CREATE TABLE salaries (
    emp_no INT NOT NULL,
    from_date DATE NOT NULL,
    salary INT NOT NULL,
    to_date DATE NOT NULL,
    PRIMARY KEY (emp_no, from_date),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no) ON DELETE CASCADE
);

-- Create titles table
CREATE TABLE titles (
    emp_no INT NOT NULL,
    title VARCHAR(50) NOT NULL,
    from_date DATE NOT NULL,
    to_date DATE,
    PRIMARY KEY (emp_no, title, from_date),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no) ON DELETE CASCADE
);
```

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/Week%2006/Lecture%2011/lecture_11/src/main/resources/data.sql). Here is the query to drop the database
```sql
-- Drop the database
DROP DATABASE IF EXISTS week6_lecture12;
```

Also don't forget to configure [application properties](/Week%2006/Lecture%2011/lecture_11/src/main/resources/application.propertiess) with this format
```java
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/<my_database>
spring.datasource.username=<my_user_name>
spring.datasource.password=<my_password>
```

and don't forget to add this
```java
spring.jpa.hibernate.ddl-auto=update
```
to do database seeding using JPA Hibernate.

### ⚙️ How to run the program
1. Go to the `lecture_12` directory by using this command
    ```bash
    $ cd lecture_12
    ```
2. Make sure you have maven installed on my computer, use `mvn -v` to check the version.
3. If you are using windows, you can run the program by using this command.
    ```bash
    $ ./run.bat
    ```
    And if you are using Linux, you can run the program by using this command.
    ```bash
    $ chmod +x run.sh
    $ ./run.sh
    ```

If all the instruction is well executed, Open [localhost:8080](http://localhost:8080) to see that the REST APIs is now works.

### 🔑 List of Endpoints
| Endpoint                                | Method | Description                                                                                 |
|-----------------------------------------|:--------: |---------------------------------------------------------------------------------------------|
| /api/v1/employees                       | GET    | Retrieve all employees with default pagination (page 0 with size 20 elements/page).                                                                     |
| /api/v1/employees?page=1&size=5         | GET    | Retrieve employees with pagination (page 2 with size 5 elements/page).                                         |
| /api/v1/employees/{empNo}               | GET    | Retrieve a specific employee by employee number.                                            |
| /api/v1/employees                       | POST   | Create a new employee.                                                                      |
| /api/v1/employees/{empNo}               | PUT    | Update an existing employee by employee number.                                             |
| /api/v1/employees/{empNo}               | DELETE | Delete an employee by employee number.                                                      |
| /api/v1/departments                     | GET    | Retrieve all departments with default pagination (page 0 with size 20 elements/page).                                                                   |
| /api/v1/departments?page=0&size=2       | GET    | Retrieve departments with pagination (page 1 with size 2 elements/page).                                       |
| /api/v1/departments/{deptNo}            | GET    | Retrieve a specific department by department number.                                        |
| /api/v1/departments                     | POST   | Create a new department.                                                                    |
| /api/v1/departments/{deptNo}            | PUT    | Update an existing department by department number.                                         |
| /api/v1/departments/{deptNo}            | DELETE | Delete a department by department number.                                                   |
| /api/v1/salaries                        | GET    | Retrieve salary by ID.                                                                      |
| /api/v1/salaries                        | POST   | Create a new salary record.                                                                 |
| /api/v1/salaries                        | PUT    | Update an existing salary record.                                                           |
| /api/v1/salaries                        | DELETE | Delete a salary record by ID.                                                               |
| /api/v1/titles                          | GET    | Retrieve title by ID.                                                                       |
| /api/v1/titles                          | POST   | Create a new title.                                                                         |
| /api/v1/titles                          | PUT    | Update an existing title.                                                                   |
| /api/v1/titles                          | DELETE | Delete a title record by ID.                                                                |

#### Additional Dynamic Search Queries
Here is some demo on how to search employees based on dynamic search queries. All the method used are `GET`.

| Endpoint                                |  Description                                                                                 |
|-----------------------------------------|---------------------------------------------------------------------------------------------|
| /api/v1/employees/search?firstName=Paul&gender=M                       | Retrieve all male employees who the first name is Paul.                                                                     |
| /api/v1/employees/search?birthDate=1991-03-22&lastName=Garcia                       | Retrieve all employees who the birth date is March 22nd, 1991 and the last name is Garcia.                                                                     |
| /api/v1/employees/search?gender=F&page=1&size=3                       | Retrieve all the female employees with pagination (page 2 with size 3 elements/page).                                                                     |
| /api/v1/employees/search                       | Retrieve all the employees data with default pagination.                                                                     |

---

### 🔥 Bonus - Advanced Query Functionality
#### Overview

This part of assignment implements advanced query functionality for the `Employee` entity. The main motivation was to allow users to perform flexible and complex searches based on various criteria, including sorting and advanced operations like greater than, less than, and querying by specific date parts (e.g., month, year). The implementation uses Spring Data JPA's Specification and Criteria API to dynamically construct queries based on the provided search criteria (basically just modifying what i already made previously).

#### Features
1. **Dynamic Filtering**: Allows filtering based on multiple criteria.
2. **Advanced Operations**: Supports operations like equals, greater than, less than, greater than or equal to, and less than or equal to.
3. **Date Part Queries**: Enables querying by specific parts of dates, such as month and year.
4. **Sorting**: Supports sorting by any field in ascending or descending order.
5. **Pagination**: Handles large datasets efficiently by providing pagination.

#### Functionalities

- Filter by first name, last name, gender, birth date, and hire date.
- Perform advanced operations (`eq`, `gt`, `lt`, `geq`, `leq`) on dates.
- Query by specific date parts (month, year).
- Sort results by any field.
- Paginate results to handle large datasets.

The updated endpoint for searching employees with advanced criteria will look like this:

##### Endpoint: `GET /api/v1/employees/search`

##### Parameters

- **page** (optional): The page number to retrieve (default is 0).
- **size** (optional): The number of elements per page (default is 20).
- **sortBy** (optional): The field to sort by (e.g., "firstName", "hireDate").
- **sortOrder** (optional): The sort order ("asc" or "desc").
- **firstName** (optional): The first name to filter by.
- **firstNameOperation** (optional): The operation for first name ("eq" for equals, "like" for like).
- **lastName** (optional): The last name to filter by.
- **lastNameOperation** (optional): The operation for last name ("eq" for equals, "like" for like).
- **gender** (optional): The gender to filter by.
- **genderOperation** (optional): The operation for gender ("eq" for equals).
- **birthDate** (optional): The birth date to filter by (format: YYYY-MM-DD).
- **birthDateOperation** (optional): The operation for birth date ("eq", "gt", "lt", "geq", "leq").
- **hireDate** (optional): The hire date to filter by (format: YYYY-MM-DD).
- **hireDateOperation** (optional): The operation for hire date ("eq", "gt", "lt", "geq", "leq").
- **birthMonth** (optional): The birth month to filter by (1-12).
- **birthYear** (optional): The birth year to filter by (e.g., 1970).
- **hireMonth** (optional): The hire month to filter by (1-12).
- **hireYear** (optional): The hire year to filter by (e.g., 2020).

##### Example Request
Here is the URL `GET` request query to do this:

Find all employees with the first name containing "John", hired after January 1st, 2020, sorted by last name in ascending order. The result will be paginated with custom pagination where maximum 10 employees/page and show the employees data on page 1 (0-based index).
```http
GET /api/v1/employees/search?page=0&size=10&sortBy=lastName&sortOrder=asc&firstName=John&firstNameOperation=like&hireDate=2020-01-01&hireDateOperation=gt
```

#### Advanced Query Examples

Here are 10 examples of advanced queries you can perform with this implementation. All the method used are `GET`.

| Endpoint                                |  Description                                                                                 |
|-----------------------------------------|---------------------------------------------------------------------------------------------|
| /api/v1/employees/search?firstName=John&firstNameOperation=like&sortBy=lastName&sortOrder=asc                       | Find employees with first name containing "John" and sort by last name ascending.                                                                     |
| /api/v1/employees/search?hireDate=2020-06-01&hireDateOperation=lt&size=40&page=2                       | Find employees hired before June 1st, 2020 with custom pagination (page 3 with size 40 elements/page).                                                                     |
| /api/v1/employees/search?birthDate=1990-01-01&birthDateOperation=gt&page=1                       | Find employees born after January 1st, 1990 with default pagination, show employees on page 2.                                                                     |
| /api/v1/employees/search?lastName=Smith&hireYear=2015                       | Find employees with last name "Smith" and hired in the year 2015.                                                                     |
| /api/v1/employees/search?birthMonth=2                       | Find employees born in February.                                                                     |
| /api/v1/employees/search?firstName=Albert&lastName=B&lastNameOperation=like&gender=M                       | Find male employees with first name "Albert" and last name starting with "B".                                                                     |
| /api/v1/employees/search?firstName=Bobby&birthDate=1985-01-01&birthDateOperation=geq                       | Find employees with first name "Bobby" and birth date greater than or equal to January 1st, 1985.                                                                     |
| /api/v1/employees/search?hireDate=2010-12-31&hireDateOperation=leq&sortBy=hireDate&sortOrder=desc&page=4                       | Find employees hired on or before December 31st, 2010, and sort by hire date descending with default pagination, show employees on page 5.                                                                     |
| /api/v1/employees/search?birthMonth=8&hireYear=2011                       | Find employees born in August and hired in the year 2011.                                                                     |
| /api/v1/employees/search?firstName=Michael&birthDate=1977-09-05&birthDateOperation=eq                       | Find employees with first name "Michael" and birth date equal to September 5th, 1977.                                                                     |

#### Remarks and Conclusion

In the industry, building flexible and advanced search functionality for REST APIs is a common practice, especially for applications that handle complex data retrieval requirements. The approach i made is in line with how such functionality is typically implemented. Here are a few points that highlight common practices in the industry:

1. **Specification and Criteria API**: Using the JPA Specification and Criteria API is a standard approach to build dynamic queries in a type-safe way. This allows for complex query construction based on various criteria, which is a common requirement in many applications.

2. **Pagination and Sorting**: Providing support for pagination and sorting in API endpoints is essential for handling large datasets. This is typically done using Spring Data's `Pageable` and `Sort` interfaces, which i've included in the `findByCriteria` method.

3. **DTOs for Search Criteria**: Using Data Transfer Objects (DTOs) to encapsulate search criteria is a common practice. This helps in structuring the input parameters and makes the API more maintainable and understandable.

4. **Combining Filters and Operations**: Allowing different operations (e.g., equality, greater than, less than) and combining them with logical operators (AND, OR) is a typical requirement for advanced search functionality. The use of predicates in the Specification API facilitates this.

5. **Documentation and Consistency**: Documenting the API endpoints and ensuring consistent parameter naming conventions is crucial. This helps other developers understand and use the API correctly.

This advanced query functionality significantly enhances the flexibility and usability of the `Employee` API. By supporting complex query operations, sorting, and pagination, it caters to a wide range of search requirements, making it a robust solution for applications that need sophisticated data retrieval capabilities.

### 📬 Postman Collection

Here is the [postman collection](/Week%2006/Lecture%2012/Assignment%2001/Lecture%2012%20-%20Assignment%2001.postman_collection.json) you can use to demo the API functionality, including the bonus part i already made on the separate APIs folder.