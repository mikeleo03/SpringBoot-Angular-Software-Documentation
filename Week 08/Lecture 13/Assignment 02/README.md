# 👨🏻‍🏫 Lecture 13 - Spring Advance Feature: Filter and Spring Interceptor
> This repository is created as a part of assignment for Lecture 13 - Spring Advance Feature: Filter and Spring Interceptor

## 🔐 Assignment 02 - Simple Filter

### 🧐 Detailed Overview

To complete the assignment, we need to add the following functionalities to the existing CRUD project:
1. **Store API key in the database**: Ensure we have a table to store the API key.
2. **Verify requests**: Filter to check for the "api-key" header in incoming requests.
3. **Include header in all responses**: Filter to add the "source" header to all responses.

Here’s a step-by-step guide:

### 📦 Storing API Key in the Database

**Idea**: Create a table to store the API key. We can have a single record in this table that holds the API key.

**Table Schema**
See the detail [here](/Week%2008/Lecture%2013/Assignment%2002/lecture_13/src/main/resources/data.sql).
```sql
-- Create `APIKey` table
CREATE TABLE APIKey (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_key VARCHAR(255) NOT NULL,
    description VARCHAR(255),         -- Description or label for the API key
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp of creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Timestamp of last update
    active BOOLEAN DEFAULT TRUE       -- Status to enable or disable the API key
);
```

**Java Entity**
See the detail [here](/Week%2008/Lecture%2013/Assignment%2002/lecture_13/src/main/java/com/example/lecture_13/data/model/APIKey.java).
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "APIKey")
public class APIKey {

    @Id
    @Column(name = "ID", columnDefinition = "BIGINT", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String apiKey;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
}
```

**Repository**
See the detail [here](/Week%2008/Lecture%2013/Assignment%2002/lecture_13/src/main/java/com/example/lecture_13/data/repository/APIKeyRepository.java).
```java
@Repository
public interface APIKeyRepository extends JpaRepository<APIKey, Long> {
    
    // Get the first API key, order by ID 
    Optional<APIKey> findFirstByOrderById();

    // Find the first active API key
    Optional<APIKey> findFirstByActiveTrueOrderById(); 
}
```

### 🔽 Filter for API Key Verification

**Idea**: Create a filter that intercepts incoming requests, checks for the "api-key" header, and verifies it against the stored API key in the database. Use it also to add the "source" header to all responses.

**Filter Implementation**
See the detail [here](/Week%2008/Lecture%2013/Assignment%2002/lecture_13/src/main/java/com/example/lecture_13/config/filter/APIKeyFilter.java).
```java
@Component
public class APIKeyFilter extends OncePerRequestFilter {

    @Autowired
    private APIKeyRepository apiKeyRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        String requestApiKey = request.getHeader("api-key");

        Optional<APIKey> apiKeyOpt = apiKeyRepository.findFirstByActiveTrueOrderById();
        response.addHeader("source", "fpt-software");
        if (apiKeyOpt.isPresent()) {
            String storedApiKey = apiKeyOpt.get().getApiKey();

            if (storedApiKey.equals(requestApiKey)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid API Key\"}");
            }
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"API Key not configured or inactive\"}");
        }
    }
}
```

### ✔️ Register Filters
**Configuration**
See the detail [here](/Week%2008/Lecture%2013/Assignment%2002/lecture_13/src/main/java/com/example/lecture_13/config/FilterConfig.java).
```java
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<APIKeyFilter> apiKeyFilter(APIKeyFilter apiKeyFilter) {
        FilterRegistrationBean<APIKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/api/v1/*"); // All Customer API
        return registrationBean;
    }
}
```

---

### 🌳 Project Structure
```bash
lecture_13
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/lecture_13/
│   │   ├── config/
│   │   │   ├── filter/
│   │   │   │   └── APIKeyFilter.java
│   │   │   ├── FilterConfig.java
│   │   │   └── WebConfig.java
│   │   ├── controller/
│   │   │   └── CustomerController.java
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   ├── APIKey.java
│   │   │   │   ├── Customer.java
│   │   │   │   └── Status.java
│   │   │   └── repository/
│   │   │       ├── APIKeyRepository.java
│   │   │       └── CustomerRepository.java
│   │   ├── dto/
│   │   │   ├── CustomerDTO.java
│   │   │   ├── CustomerSaveDTO.java
│   │   │   └── CustomerShowDTO.java
│   │   ├── exception/
│   │   │   ├── BadRequestException.java
│   │   │   ├── DuplicateStatusException.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── ResourceNotFound.java
│   │   ├── mapper/
│   │   │   └── CustomerMapper.java
│   │   ├── service/
│   │   │   ├── impl/
│   │   │   │   └── CustomerServiceImpl.java
│   │   │   └── CustomerService.java
│   │   └── Lecture13Application.java
│   └── resources/
│       ├── application.properties
│       └── data.sql
├── .gitignore
├── env.properties
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
```

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/Week%2008/Lecture%2013/Assignment%2002/lecture_13/src/main/resources/data.sql). Here is the query to drop the database.
```sql
-- Drop the database
DROP DATABASE IF EXISTS week8_lecture13;
```

Don't forget to add this to re-update the SQL DDL queries.
```java
spring.jpa.hibernate.ddl-auto=update
```

finally, don't forget to add this for hibernate SQL logging.
```java
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### ⚙️ How to run the program
1. Go to the `lecture_13` directory by using this command
    ```bash
    $ cd lecture_13
    ```
2. Make sure you have maven installed on my computer, use `mvn -v` to check the version.
3. Setup your credential. You can configure it by creating file `env.properties` on the **root of the project** (aligned with [pom.xml](/Week%2008/Lecture%2013/Assignment%2002/lecture_13/pom.xml)), then fill it with this format.
    ```java
    DB_DATABASE=<your database url>
    DB_USER=<your database username>
    DB_PASSWORD=M<your database password>
    PORT=<your preferred port number>
    ```
4. If you are using windows, you can run the program by using this command.
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
| Endpoints                                                                                  | Method | Description                                                                                     |
|--------------------------------------------------------------------------------------------|:------:|-------------------------------------------------------------------------------------------------|
| /api/v1/customers                                                                          | GET    | Retrieve all customers with default pagination (page 1 with size 20 elements/page). Consider only active customers. |
| /api/v1/customers?page={X}&size={Y}                                                        | GET    | Retrieve all customers with custom pagination (page X (0-based index) with size Y elements/page). Consider only active customers. |
| /api/v1/customers                                                                          | POST   | Create a new customer. Validate POST request format.                                            |
| /api/v1/customers/{id}                                                                     | PUT    | Update an existing customer by customer ID. Make sure the customer ID exists.                   |
| /api/v1/customers/active/{id}                                                              | PUT    | Activate the existing customer by their customer ID. Make sure the customer ID exists and is currently inactive. |
| /api/v1/customers/deactive/{id}                                                            | PUT    | Deactivate the existing customer by their customer ID. Make sure the customer ID exists and is currently active. |
| /api/v1/customers/{id}                                                            | DELETE    | Delete an existing customer by customer ID. Make sure the customer ID exists. |


### 🚀 Demonstration
![Screenshots](/Week%2008/Lecture%2013/Assignment%2002/img/demo1.png)

Result if API Key not exists or not configured on the header.

![Screenshots](/Week%2008/Lecture%2013/Assignment%2002/img/demo2.png)

Result if API Key well-configured on the header.

![Screenshots](/Week%2008/Lecture%2013/Assignment%2002/img/demo3.png)

Result header returning source from fpt-software.