# 👨🏻‍🏫 Lecture 17 - Microservices: Spring Cloud Gateway
> This repository is created as a part of the assignment for Lecture 17 - Microservices: Spring Cloud Gateway

## 🔐 Assignment 02 - Gateway Authentication Filter

### 🧐 Detailed Overview

In this task, i'm adding an authentication layer to my microservices architecture using Spring Cloud Gateway. The goal is to ensure that each request passing through the gateway includes a valid "api-key" in the header. This involves:
1. **Adding a filter to the Gateway**: This filter will intercept incoming requests and verify if they contain the "api-key" header. If the header is present, it will forward the request to an authentication service to validate the key.
2. **Creating an Authentication Service**: This service will store the valid "api-key(s)" in a database and handle the validation logic when the Gateway calls it.
3. **Configuring the "api-key" in a Database**: I store the valid "api-key(s)" in a database table, which the authentication service will query to validate incoming requests.

### 🛠️ Implementation Details

1. **Setting Up the Authentication Service Project**

    First, create a new Spring Boot project for the auth service. Add the necessary dependencies in the `pom.xml`:
   
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    ```

2. **Create the Authentication Service**

    This service will manage and validate the "api-key" stored in the database.

    **a. Create the API Key Entity and Repository:**

    Here is the detail of [API Key Entity](/Week%2010/Lecture%2017/Assignment%2002/authentication/src/main/java/com/example/authentication/data/model/ApiKey.java)
    
    ```java
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "ApiKey")
    public class ApiKey {

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

    Here is the detail of [API Key Repository](/Week%2010/Lecture%2017/Assignment%2002/authentication/src/main/java/com/example/authentication/data/repository/ApiKeyRepository.java)

    ```java
    @Repository
    public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
        
        // Get the first API key, order by ID 
        Optional<ApiKey> findFirstByOrderById();

        // Find the first active API key
        Optional<ApiKey> findFirstByActiveTrueOrderById(); 
    }
    ```

    **b. Create the Authentication Service:**

    Here is the detail of [API Key Service](/Week%2010/Lecture%2017/Assignment%2002/authentication/src/main/java/com/example/authentication/service/ApiKeyService.java)

    ```java
    @Service
    public class ApiKeyServiceImpl implements ApiKeyService {

        private final ApiKeyRepository apiKeyRepository;

        @Autowired
        public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository) {
            this.apiKeyRepository = apiKeyRepository;
        }

        @Override
        public boolean isValidApiKey(String requestApiKey) {
            // Check from the repo
            Optional<ApiKey> apiKeyOpt = apiKeyRepository.findFirstByActiveTrueOrderById();
            if (apiKeyOpt.isPresent()) {
                // Check if it's the same
                String storedApiKey = apiKeyOpt.get().getApiKey();
                return storedApiKey.equals(requestApiKey);
            }
            return false;
        }
    }
    ```

    **c. Create the Controller:**

    Here is the detail of [API Key Controller](/Week%2010/Lecture%2017/Assignment%2002/authentication/src/main/java/com/example/authentication/controller/ApiKeyController.java)

    ```java
    @RestController
    @RequestMapping("/api/v1/auth")
    @Validated
    public class ApiKeyController {

        private final ApiKeyService apiKeyService;

        @Autowired
        public ApiKeyController(ApiKeyService apiKeyService) {
            this.apiKeyService = apiKeyService;
        }

        @Operation(summary = "Validate API Key.")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API Key is valid"),
            @ApiResponse(responseCode = "401", description = "API Key is invalid")
        })
        @GetMapping("/validate")
        public ResponseEntity<Boolean> validateApiKey(@RequestParam String key) {
            boolean isValid = apiKeyService.isValidApiKey(key);
            return ResponseEntity.ok(isValid);
        }
    }
    ```

3. **Configure the Gateway with the Filter**

    **a. Create the Filter:**

    I create a custom filter to intercept requests and validate the "api-key." in my Gateway project.

    Here is the detail of [API Filter](/Week%2010/Lecture%2017/Assignment%2002/gateway/src/main/java/com/example/gateway/ApiKeyGatewayFilterFactory.java)

    ```java
    @Component
    public class ApiKeyGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiKeyGatewayFilterFactory.Config> {

        private static final String API_KEY_HEADER = "api-key";
        private final AuthClient authClient;

        @Autowired
        public ApiKeyGatewayFilterFactory(AuthClient authClient) {
            super(Config.class);
            this.authClient = authClient;
        }

        @Override
        public GatewayFilter apply(Config config) {
            return (exchange, chain) -> {
                String apiKey = exchange.getRequest().getHeaders().getFirst(API_KEY_HEADER);
                if (apiKey == null) {
                    return Mono.just(exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)).then();
                }

                return authClient.validateApiKey(apiKey)
                    .flatMap(isValid -> {
                        if (!isValid) {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return Mono.empty();
                        } else {
                            return chain.filter(exchange);
                        }
                    });
            };
        }

        @Override
        public Config newConfig() {
            return new Config();
        }

        public static class Config {
            // Configuration properties (if any) can be added here
        }
    }
    ```

    **b. Create the AuthClient:**

    I create an AuthClient to get and validate whether the given API Key is valid or not to the authentication service.

    Here is the detail of [AuthClient](/Week%2010/Lecture%2017/Assignment%2002/gateway/src/main/java/com/example/gateway/client/AuthClient.java)

    ```java
    @Service
    public class AuthClient {

        private final WebClient webClient;

        @Autowired
        public AuthClient(WebClient.Builder webClientBuilder) {
            this.webClient = webClientBuilder.baseUrl("http://localhost:8083/api/v1/auth").build();
        }

        public Mono<Boolean> validateApiKey(String apiKey) {
            return webClient.get()
                    .uri("/validate?key=" + apiKey)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .onErrorResume(WebClientResponseException.class, ex -> {
                        if (ex.getStatusCode().is4xxClientError()) {
                            return Mono.just(false);
                        }
                        return Mono.error(ex);
                    });
        }        
    }
    ```

    **c. Register the Filter:**

    In the [`application.yml`](/Week%2010/Lecture%2017/Assignment%2002/gateway/src/main/resources/application.yml) of the Gateway service, ensure that the custom filter is registered:

    ```yaml
    server:
        port: 8080  # Gateway server port

    spring:
        application:
            name: gateway-service

        cloud:
            gateway:
            routes:
                - id: product-service
                uri: http://localhost:8081
                predicates:
                    - Path=/api/v1/products/**
                filters:
                    - name: ApiKey

                - id: customer-service
                uri: http://localhost:8082
                predicates:
                    - Path=/api/v1/customers/**
                filters:
                    - name: ApiKey
                    
    management:
        endpoints:
            web:
            exposure:
                include: "*"
    ```

### 📚 Summary

This implementation provides a secure way to control access to my microservices by verifying an "api-key" through the Spring Cloud Gateway. The Authentication service centralizes the management of valid API keys, making it easier to maintain and update keys as needed.

---

### 🏛️ Project Architecture
<a href="https://raw.githubusercontent.com/affandyfandy/java-leon/Week_10/Week%2010/Lecture%2017/Assignment%2002/img/architecture.png">
    <img src="/Week 10/Lecture 17/Assignment 02/img/architecture.png" target="_blank" />
    </a>
<br />

> Click image to enlarge.

### 🌳 Project Structure
#### 1. Product Service
```bash
product
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/product/
│   │   ├── config/
│   │   │   └── WebConfig.java
│   │   ├── controller/
│   │   │   └── ProductController.java
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   ├── CustomerProduct.java
│   │   │   │   ├── Product.java
│   │   │   │   └── Status.java
│   │   │   └── repository/
│   │   │       ├── CustomerProductRepository.java
│   │   │       └── ProductRepository.java
│   │   ├── dto/
│   │   │   ├── CustomerProductDTO.java
│   │   │   ├── ProductDTO.java
│   │   │   ├── ProductSaveDTO.java
│   │   │   ├── ProductSearchCriteriaDTO.java
│   │   │   └── ProductShowDTO.java
│   │   ├── exception/
│   │   │   ├── BadRequestException.java
│   │   │   ├── DuplicateStatusException.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── InsufficientQuantityException.java
│   │   │   └── ResourceNotFoundException.java
│   │   ├── mapper/
│   │   │   └── ProductMapper.java
│   │   ├── service/
│   │   │   ├── impl/
│   │   │   │   └── ProductServiceImpl.java
│   │   │   └── ProductService.java
│   │   └── ProductApplication.java
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

#### 2. Customer Service
```bash
customer
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/customer/
│   │   ├── client/
│   │   │   └── ProductClient.java
│   │   ├── config/
│   │   │   ├── WebClientConfig.java
│   │   │   └── WebConfig.java
│   │   ├── controller/
│   │   │   └── CustomerController.java
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   ├── Customer.java
│   │   │   │   ├── CustomerProduct.java
│   │   │   │   └── Status.java
│   │   │   └── repository/
│   │   │       ├── CustomerProductRepository.java
│   │   │       └── CustomerRepository.java
│   │   ├── dto/
│   │   │   ├── CustomerDTO.java
│   │   │   ├── CustomerProductDTO.java
│   │   │   ├── CustomerProductSaveDTO.java
│   │   │   ├── CustomerSaveDTO.java
│   │   │   └── ProductDTO.java
│   │   ├── exception/
│   │   │   ├── BadRequestException.java
│   │   │   ├── DuplicateStatusException.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── InsufficientQuantityException.java
│   │   │   └── ResourceNotFoundException.java
│   │   ├── mapper/
│   │   │   └── CustomerMapper.java
│   │   ├── service/
│   │   │   ├── impl/
│   │   │   │   └── CustomerServiceImpl.java
│   │   │   └── CustomerService.java
│   │   └── CustomerApplication.java
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

#### 3. Spring Gateway
```bash
gateway
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/gateway/
│   │   ├── client/
│   │   │   └── AuthClient.java
│   │   ├── ApiKeyGatewayFilterFactory.java
│   │   └── GatewayApplication.java
│   └── resources/
│       ├── application.properties
│       └── application.yml
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
├── run.bat
└── run.sh
```

#### 4. Authentication Service
```bash
authentication
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/authentication/
│   │   ├── controller/
│   │   │   └── ApiKeyController.java
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   └── ApiKey.java
│   │   │   └── repository/
│   │   │       └── ApiKeyRepository.java
│   │   ├── service/
│   │   │   ├── impl/
│   │   │   │   └── ApikeyServiceImpl.java
│   │   │   └── ApiKeyService.java
│   │   └── AuthenticationApplication.java
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

#### 1. Product Service
```sql
-- Create the database
CREATE DATABASE week10_product;

-- Use the database
USE week10_product;

-- Initialize table with DDLs
-- Create `Product` table
CREATE TABLE Product (
    ID VARCHAR(36) PRIMARY KEY,    -- Use VARCHAR for Strings
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    status VARCHAR(50) NOT NULL,   -- Use VARCHAR instead of ENUM
    quantity INT,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP
);

-- Create `CustomerProduct` table
CREATE TABLE CustomerProduct (
    id VARCHAR(36) PRIMARY KEY, -- Unique identifier for each record
    customerId VARCHAR(36) NOT NULL, -- ID of the customer
    productId VARCHAR(36) NOT NULL, -- ID of the product
    quantity INT NOT NULL, -- Quantity of the product purchased
    purchasedAt TIMESTAMP, -- Timestamp of purchase
    FOREIGN KEY (customerId) REFERENCES Customer(ID)
);
```

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/Week%2010/Lecture%2017/Assignment%2002/product/src/main/resources/data.sql). Here is the query to drop the database.
```sql
-- Drop the database
DROP DATABASE IF EXISTS week10_product;
```

#### 2. Customer Service
```sql
-- Create the database
CREATE DATABASE week10_customer;

-- Use the database
USE week10_customer;

-- Initialize table with DDLs
-- Create `Customer` table
CREATE TABLE Customer (
    ID VARCHAR(36) PRIMARY KEY,    -- Use VARCHAR for Strings
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP
);

-- Create `CustomerProduct` table
CREATE TABLE CustomerProduct (
    id VARCHAR(36) PRIMARY KEY, -- Unique identifier for each record
    customerId VARCHAR(36) NOT NULL, -- ID of the customer
    productId VARCHAR(36) NOT NULL, -- ID of the product
    quantity INT NOT NULL, -- Quantity of the product purchased
    purchasedAt TIMESTAMP, -- Timestamp of purchase
    FOREIGN KEY (customerId) REFERENCES Customer(ID)
);
```

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/Week%2010/Lecture%2017/Assignment%2002/customer/src/main/resources/data.sql). Here is the query to drop the database.
```sql
-- Drop the database
DROP DATABASE IF EXISTS week10_customer;
```

#### 3. Authentication Service
```sql
-- Create the database
CREATE DATABASE week10_auth;

-- Use the database
USE week10_auth;

-- Initialize table with DDLs
-- Create `ApiKey` table
CREATE TABLE ApiKey (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_key VARCHAR(255) NOT NULL,
    description VARCHAR(255),         -- Description or label for the API key
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp of creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Timestamp of last update
    active BOOLEAN DEFAULT TRUE       -- Status to enable or disable the API key
);
```

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/Week%2010/Lecture%2017/Assignment%2002/authentication/src/main/resources/data.sql). Here is the query to drop the database.
```sql
-- Drop the database
DROP DATABASE IF EXISTS week10_auth;
```

#### 4. Application Properties
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
1. Go to the each directory one by one (customer, product, gateway), by using this command
    ```bash
    $ cd <dir>
    ```
2. Make sure you have maven installed on my computer, use `mvn -v` to check the version.
3. Setup your credential. You can configure it by creating file `env.properties` on the **root of the each service project (customer, product, and authentication)**, aligned with pom.xml, then fill it with this format.
    ```java
    DB_DATABASE=<your database url>
    DB_USER=<your database username>
    DB_PASSWORD=<your database password>
    PORT=<your preferred port number>
    ```
4. If you are using windows, you can run the program **on each directory** by using this command.
    ```bash
    $ ./run.bat
    ```
    And if you are using Linux, you can run the program by using this command.
    ```bash
    $ chmod +x run.sh
    $ ./run.sh
    ```

If all the instruction is well executed, The API Gateway will be executed on [localhost:8080](http://localhost:8080), Product Service will be on [localhost:8081](http://localhost:8081), Customer Service will be on [localhost:8082](http://localhost:8082), and Authentication Service will be on [localhost:8083](http://localhost:8083). Go check them out to see that the Cloud Gateway is now works.

### 🔑 List of Endpoints
#### 1. Product Service ([localhost:8081](http://localhost:8081))

Access the swagger [here](http://localhost:8081/swagger-ui/index.html)

![Screenshots](/Week%2010/Lecture%2017/Assignment%2001/img/product.png)

#### 2. Customer Service ([localhost:8082](http://localhost:8082))

Access the swagger [here](http://localhost:8082/swagger-ui/index.html)

![Screenshots](/Week%2010/Lecture%2017/Assignment%2001/img/customer.png)


### 🚀 Demonstration
This demonstration will demo request which directed from API Gateway into the Customer Service, then Customer Service call Product Service through WebClient, and then return the result back to the API Gateway. All the demo will use (`GET /api/v1/customers/{customerId}/products`) to the Gateway [localhost:8080](http://localhost:8080). 

Here is the sequence diagram of the flow.

<a href="https://raw.githubusercontent.com/affandyfandy/java-leon/Week_10/Week%2010/Lecture%2017/Assignment%2002/img/sequence.png">
    <img src="/Week 10/Lecture 17/Assignment 02/img/sequence.png" target="_blank" />
</a>
> Click image to enlarge.

#### 1. Without "api-key"
![Screenshots](/Week%2010/Lecture%2017/Assignment%2002/img/without.png)

#### 2. With invalid API-key
![Screenshots](/Week%2010/Lecture%2017/Assignment%2002/img/invalid.png)

#### 3. With valid API-key but inactive
![Screenshots](/Week%2010/Lecture%2017/Assignment%2002/img/inactive.png)

#### 4. With valid and active API-key
![Screenshots](/Week%2010/Lecture%2017/Assignment%2002/img/active.png)