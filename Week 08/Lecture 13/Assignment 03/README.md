# 👨🏻‍🏫 Lecture 13 - Spring Advance Feature: Filter and Spring Interceptor
> This repository is created as a part of assignment for Lecture 13 - Spring Advance Feature: Filter and Spring Interceptor

## 👀 Assignment 03 - Simple Interceptor

### 🧐 Detailed Overview

1. **Store Username for Each API Key**: Add a username field to the `ApiKey` entity and include this in the response headers.
2. **Add Timestamp Header**: Add a `timestamp` header with the current time to all responses.
3. **Store Last Used Time**: Update the `ApiKey` entity to store the last time it was used.
4. **Print Username in Controller**: Implement a method in the controller to print the username associated with the API key.

### 🛠️ Modify the `ApiKey` Entity

**Idea**: Update the `ApiKey` entity to include a username field and a last used time field.

**Java Entity**
See the detail [here](/Week%2008/Lecture%2013/Assignment%2003/lecture_13/src/main/java/com/example/lecture_13/data/model/APIKey.java).
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
    private String username; // Added username field
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
    private LocalDateTime lastUsedAt; // Added last used field
}
```

### 🗄️ Update the Repository

**Idea**: Add a method to update the last used time.

**Repository**
See the detail [here](/Week%2008/Lecture%2013/Assignment%2003/lecture_13/src/main/java/com/example/lecture_13/data/repository/APIKeyRepository.java).
```java
@Repository
public interface APIKeyRepository extends JpaRepository<APIKey, Long> {
    
    // Get the first API key, order by ID 
    Optional<APIKey> findFirstByOrderById();

    // Find the first active API key
    Optional<APIKey> findFirstByActiveTrueOrderById();

    // Find the first active API key with a specific API key
    Optional<APIKey> findFirstByApiKeyAndActiveTrue(String apiKey); 
}
```

### 🌐 Create Interceptor

**Idea**: Create an interceptor that intercepts incoming requests, checks for the "api-key-username" header, send it to header, and also setup "timestamp" header to all responses.

**Interceptor Implementation**
See the detail [here](/Week%2008/Lecture%2013/Assignment%2003/lecture_13/src/main/java/com/example/lecture_13/config/interceptor/APIKeyInterceptor.java).
```java
@Component
public class APIKeyInterceptor implements HandlerInterceptor {

    private final APIKeyRepository apiKeyRepository;
    private final static Logger logger = LoggerFactory.getLogger(APIKeyInterceptor.class);

    @Autowired
    public APIKeyInterceptor(APIKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
        logger.info("APIKeyRepository injected: {}", (this.apiKeyRepository != null));
    }

    // Request is intercepted by this method before reaching the Controller
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Pre-processing logic here
        // Apply timestamp
        response.addHeader("timestamp", LocalDateTime.now().toString());
        logger.info("[PreHandle] Timestamp applied.");
        logger.info("[PreHandle][" + request + "]" + "[" + request.getMethod()+ "] " + request.getRequestURI());
        return true; // Continue with the request
    }

    // Response is intercepted by this method before reaching the client
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Post-handle logic here
        logger.info("[PostHandle][" + request + "]" + "[" + request.getMethod()+ "] " + request.getRequestURI());
    }

    // This method is called after request & response HTTP communication is done.
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("[AfterCompletion][" + request + "]" + "[" + request.getMethod()+ "] " + request.getRequestURI());

        String username = request.getHeader("api-key-username");
        if (username != null) {
            response.addHeader("username", username);
            logger.info("[AfterCompletion] Username detected : {}", username);
        }

        // Update lastUsedAt in the database
        String requestApiKey = request.getHeader("api-key");
        logger.info("[AfterCompletion] API Key : {}", requestApiKey);
        Optional<APIKey> apiKeyOpt = apiKeyRepository.findFirstByApiKeyAndActiveTrue(requestApiKey);
        if (apiKeyOpt.isPresent()) {
            APIKey apiKey = apiKeyOpt.get();
            apiKey.setUsername(username);
            apiKey.setLastUsedAt(LocalDateTime.now());
            apiKeyRepository.save(apiKey);
            logger.info("[AfterCompletion] API Key data updated : {}", apiKey);
        }

        logger.info("[AfterCompletion] Execution done.");
    }
}
```

### ✔️ Register Interceptor
**Interceptor Configuration**
See the detail [here](/Week%2008/Lecture%2013/Assignment%2003/lecture_13/src/main/java/com/example/lecture_13/config/InterceptorConfig.java).
```java
@Configuration
public class InterceptorConfig implements WebMvcConfigurer { 
    // Register an interceptor with the registry
	private final APIKeyInterceptor apiKeyInterceptor;

    @Autowired
    public InterceptorConfig(APIKeyInterceptor apiKeyInterceptor) {
        this.apiKeyInterceptor = apiKeyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor);
    }
}
```

#### Response Header Filter

Update the response header filter to include the `timestamp` header.

### 🆕 Add a Controller Method to Print Username

**Idea**: Add a method to your controller to print the username associated with the API key.

**Customer Controller**
See the detail [here](/Week%2008/Lecture%2013/Assignment%2003/lecture_13/src/main/java/com/example/lecture_13/controller/CustomerController.java).

```java
@RestController
@RequestMapping("/api/v1/customers")
@Validated
public class CustomerController {

    .....

    /**
     * Retrieves the username of the current API User.
     *
     * @param apiKey The API key provided in the request header.
     * @return The username of the current API User if the provided API key is valid, otherwise returns "Invalid API Key".
     */
    @Operation(summary = "Get the username of current API User.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Username retrieved successfully"),
    })
    @GetMapping("/username")
    public String printUsername(@RequestHeader("api-key") String apiKey) {
        Optional<APIKey> apiKeyOpt = apiKeyRepository.findFirstByActiveTrueOrderById();
        if (apiKeyOpt.isPresent() && apiKeyOpt.get().getApiKey().equals(apiKey)) {
            return "Username: " + apiKeyOpt.get().getUsername();
        }
        return "Invalid API Key";
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
│   │   │   ├── interceptor/
│   │   │   │   └── APIKeyInterceptor.java
│   │   │   ├── FilterConfig.java
│   │   │   ├── InterceptorConfig.java
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
    active BOOLEAN DEFAULT TRUE,      -- Status to enable or disable the API key
    last_used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Last time the API key was used
    username VARCHAR(255)             -- Username associated with the API key
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
| /api/v1/customers/username                                                            | GET    | Get the current username of Customer API user. |


### 🚀 Demonstration
#### Screenshots
![Screenshots](/Week%2008/Lecture%2013/Assignment%2003/img/demo1.png)

The request headers used and the response headers returned.

![Screenshots](/Week%2008/Lecture%2013/Assignment%2003/img/demo2.png)

Data stored on APIKey table from database.

![Screenshots](/Week%2008/Lecture%2013/Assignment%2003/img/demo3.png)

The current Customer API username.

#### Swagger Documentation
I use swagger for documentation, you can access it [here](http://localhost:8080/swagger-ui/index.html)

![Screenshots](/Week%2008/Lecture%2013/Assignment%2003/img/swagger.png)

#### Logs
Here is the log printed from the console (excluding the SQL Hibernate log) when `GET` request to `/api/v1/customers` is called with the defined headers.
```java
[Filter][org.apache.catalina.connector.RequestFacade@73cd94a6][GET] /api/v1/customers
[Filter] doFilter starts.
[PreHandle][org.apache.catalina.connector.RequestFacade@73cd94a6][GET] /api/v1/customers
[PreHandle] Timestamp applied.
[PreHandle] preHandle done.
[PostHandle][org.apache.catalina.connector.RequestFacade@73cd94a6][GET] /api/v1/customers
[PostHandle] postHandle done.
[AfterCompletion][org.apache.catalina.connector.RequestFacade@73cd94a6][GET] /api/v1/customers
[AfterCompletion] Username detected : Leon
[AfterCompletion] API Key : 12345-ABCDE
[AfterCompletion] API Key data updated : APIKey(id=1, apiKey=12345-ABCDE, username=Leon, description=Primary API Key for System Access, createdAt=2024-07-29T15:12:25, updatedAt=2024-07-29T15:12:25, active=true, lastUsedAt=2024-07-30T10:53:09.157716200)
[AfterCompletion] afterCompletion done.
[Filter] doFilter done.
[Filter] Logging Response : 200
```