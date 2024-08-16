# 👨🏻‍🏫 Lecture 18 - Microservices: Discovery Service Integration
> This repository is created as a part of the assignment for Lecture 18 - Microservices: Discovery Service Integration

## 👀 Assignment 01 - Updating Microservices to Use Discovery Service

### 🧐 Detailed Overview
In this assignment, i will update the existing microservices architecture to incorporate a discovery service. This will allow the services to register themselves and discover other services dynamically without needing to hard-code their locations.

The main components i will focus on are:
1. **Discovery Service (Eureka Server)**
2. **Service Registration (Eureka Client) with Dynamic Port Allocation**
3. **Integrating Discovery Service with API Gateway**

### 🛠️ Implementation Details

1. **Setting Up the Discovery Service (Eureka Server)**
   
   I will use Spring Cloud Netflix Eureka as the discovery service. This service will act as a registry where all the microservices will register themselves.

    - **Add Eureka Server dependency in `pom.xml`:**
   
        ```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        ```

   - **Configure Eureka Server in the main application class:**
   
        ```java
        @SpringBootApplication
        @EnableEurekaServer
        public class DiscoveryApplication {

            public static void main(String[] args) {
                SpringApplication.run(DiscoveryApplication.class, args);
            }
        }
        ```

   - **Add configuration in `application.properties`:**
   
        ```yaml
        # Port
        server.port=8761

        # Eureka setup
        eureka.client.register-with-eureka=false
        eureka.client.fetch-registry=false
        ```

   This configuration sets up the Eureka Server on port `8761`.

2. **Registering Microservices with Eureka (Eureka Client)**

   Each microservice needs to register itself with the Eureka Server so that it can be discovered by other services.

   - **Add Eureka Client dependency in `pom.xml`:**
   
     ```xml
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
     </dependency>
     ```

    - **Configure Eureka Client in the microservices' main application class:**
   
        For this explanation on Product Service
        
        ```java
        @SpringBootApplication
        @EnableDiscoveryClient
        public class ProductApplication {
            public static void main(String[] args) {
                SpringApplication.run(ProductApplication.class, args);
            }
        }
        ```

   - **Add configuration in `application.properties` for each microservice:**
   
        ```yaml
        # Port
        server.port=0

        # Eureka setup
        eureka.instance.hostname=localhost
        eureka.instance.instance-id=${spring.application.name}
        ```

   Repeat similar steps for other microservices (e.g., `customer-service`, etc.), changing the `server.port` to 0 to make it allocated dynamically and `spring.application.name` accordingly to highlight the service.

3. **Using Discovery Client for Service Communication**

   Once the microservices are registered with Eureka, i can use Spring Cloud's `DiscoveryClient` to dynamically discover services.

    - **Example of using `Eureka` in ProductClient for Customer Service:**
   
        ```java
        @Service
        public class ProductClient {

            private final WebClient.Builder webClientBuilder;
            private final EurekaClient eurekaClient;
            private static final String SERVICE_NAME = "product-service";

            @Autowired
            public ProductClient(EurekaClient eurekaClient, WebClient.Builder webClientBuilder) {
                this.eurekaClient = eurekaClient;
                this.webClientBuilder = webClientBuilder;
            }

            /**
            * Retrieves the base URL of the Product service from Eureka.
            *
            * @return The base URL as a string.
            */
            private String getServiceUrl() {
                InstanceInfo service = eurekaClient
                    .getApplication(SERVICE_NAME)
                    .getInstances()
                    .get(0);

                String hostName = service.getHostName();
                int port = service.getPort();

                return "http://" + hostName + ":" + port + "/api/v1/products";
            }
        }
        ```

    This allows us to fetch the available instances of a service dynamically.

4. **Integrating the gateway with discover service**

    Here is the setup for `application.properties`:
    ```xml
    # Eureka setup
    eureka.instance.hostname=localhost
    eureka.instance.instance-id=${spring.application.name}

    spring.cloud.gateway.discovery.locator.enabled=true
    spring.cloud.gateway.discovery.locator.lower-case-service-id=true
    ```

    And this is for `application.yml`:
    ```xml
    server:
        port: 8080  # Gateway server port

    spring:
        cloud:
            gateway:
                routes:
                    - id: product-service
                    uri: lb://product-service
                    predicates:
                        - Path=/api/v1/products/**
                    filters:
                        - name: ApiKey

                    - id: customer-service
                    uri: lb://customer-service
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

5. **Testing the Setup**

   Once the above steps are completed:
   - Start the Eureka Server.
   - Start all the microservices.
   - Visit the Eureka Dashboard at `http://localhost:8761` to see all registered services.

### 📚 Summary

By integrating a discovery service (Eureka) into the microservices architecture, i've enabled dynamic service discovery, simplified service communication, and introduced client-side load balancing. This setup makes the architecture more scalable and resilient, reducing the need for hardcoded service URLs and allowing for better handling of multiple service instances.

---

### 🏛️ Project Architecture
<a href="https://raw.githubusercontent.com/affandyfandy/java-leon/Week_10/Week%2010/Lecture%2018/img/architecture.png">
    <img src="/Week 10/Lecture 18/img/architecture.png" target="_blank" />
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

#### 5. Discovery Service
```bash
discovery
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/discovery/
│   │   └── DiscoveryApplication.java
│   └── restheces/
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

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/Week%2010/Lecture%2018/product/src/main/resources/data.sql). Here is the query to drop the database.
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

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/Week%2010/Lecture%2018/customer/src/main/resources/data.sql). Here is the query to drop the database.
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

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/Week%2010/Lecture%2018/authentication/src/main/resources/data.sql). Here is the query to drop the database.
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
1. Go to the each directory one by one (customer, product, gateway, authentication, and discovery), by using this command
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

If all the instruction is well executed, The API Gateway will be executed on [localhost:8080](http://localhost:8080), Discovery service will be on [localhost:8761](http://localhost:8761). Product Service, Customer Service, and Authentication Service will be **dynamically allocated**. Go check them out to see that the Cloud Gateway is now works.

### 🔑 List of Endpoints
#### 1. Product Service

![Screenshots](/Week%2010/Lecture%2018/img/product.png)

#### 2. Customer Service

![Screenshots](/Week%2010/Lecture%2018/img/customer.png)


### 🚀 Demonstration
Here is what it looks like from the Eureka Client on [localhost:8761](http://localhost:8761).

![Screenshots](/Week%2010/Lecture%2018/img/eureka-1.png)

![Screenshots](/Week%2010/Lecture%2018/img/eureka-2.png)

This demonstration will demo request which directed from API Gateway into the Customer Service, then Customer Service call Product Service through WebClient, and then return the result back to the API Gateway. All the demo will use (`GET /api/v1/customers/{customerId}/products`) to the Gateway [localhost:8080](http://localhost:8080). 

Here is the sequence diagram of the flow.

<a href="https://raw.githubusercontent.com/affandyfandy/java-leon/Week_10/Week%2010/Lecture%2018/img/sequence.png">
    <img src="/Week 10/Lecture 18/img/sequence.png" target="_blank" />
</a>

> Click image to enlarge.

#### 1. Without "api-key"
![Screenshots](/Week%2010/Lecture%2018/img/without.png)

#### 2. With invalid API-key
![Screenshots](/Week%2010/Lecture%2018/img/invalid.png)

#### 3. With valid API-key but inactive
![Screenshots](/Week%2010/Lecture%2018/img/inactive.png)

#### 4. With valid and active API-key
![Screenshots](/Week%2010/Lecture%2018/img/active.png)