# 👨🏻‍🏫 Lecture 17 - Microservices: Spring Cloud Gateway
> This repository is created as a part of the assignment for Lecture 17 - Microservices: Spring Cloud Gateway

## 💭 Assignment 01 - Spring Cloud Gateway

### 🧐 Detailed Overview

**Spring Cloud Gateway** is an API Gateway built on top of Spring WebFlux, providing a simple yet powerful way to route and manage traffic to your microservices. It offers features like path rewriting, load balancing, request rate limiting, and more. As a reactive gateway, it efficiently handles concurrent requests, making it an excellent choice for microservices architectures.

### 🔎 Why Use Spring Cloud Gateway?

- **Routing and Load Balancing:** Automatically route requests to appropriate services and distribute load evenly across instances.
- **Security:** Implement security features like OAuth2 authentication at the gateway level.
- **Monitoring and Logging:** Easily monitor and log requests passing through the gateway.
- **Transformation:** Modify incoming and outgoing requests or responses on the fly.

### 🛠️ Implementation Details

1. **Setting Up the Spring Cloud Gateway Project**

   First, create a new Spring Boot project for the gateway service. Add the necessary dependencies in your `pom.xml`:
   
   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-webflux</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-gateway</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-actuator</artifactId>
       </dependency>
   </dependencies>
   ```

2. **Configuring the `application.yml`**

   Place the `application.yml` in the `src/main/resources` directory. This file defines the gateway routes and configurations. Here’s an example configuration:
   
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

            - id: customer-service
            uri: http://localhost:8082
            predicates:
                - Path=/api/v1/customers/**
    ```

   - **`product-service` Route:** Maps all requests with `/api/v1/products/**` to the Product service running on port `8081`.
   - **`customer-service` Route:** Maps all requests with `/api/v1/customers/**` to the Customer service running on port `8082`.

3. **Running the Gateway**

   Ensure that Product service (8081), and Customer service (8082) are running. Then start the API Gateway service. The gateway will automatically route incoming requests to the appropriate service based on the path.

5. **Handling Errors and Monitoring**

   Spring Cloud Gateway allows you to customize error responses and integrate with monitoring tools like Spring Boot Actuator. For instance, you can handle errors like insufficient product quantity in the Product service and propagate these errors back through the gateway.

   You can also expose gateway metrics by enabling Actuator:
   
   ```yaml
   management:
     endpoints:
       web:
         exposure:
           include: "*"
   ```

   This provides insights into gateway performance, routing, and more.

### 📝 Example Code Implementation

```java
package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

- **`ApiGatewayApplication`:** The main class to start the Spring Boot application for the gateway.

### 📚 Summary

Spring Cloud Gateway serves as the entry point to your microservices, handling requests, applying filters, and managing traffic efficiently. By configuring routes and leveraging its robust feature set, you can build a scalable and secure microservices architecture.

---

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

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/Week%2010/Lecture%2017/Assignment%2001/product/src/main/resources/data.sql). Here is the query to drop the database.
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

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/Week%2010/Lecture%2017/Assignment%2001/customer/src/main/resources/data.sql). Here is the query to drop the database.
```sql
-- Drop the database
DROP DATABASE IF EXISTS week10_customer;
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
1. Go to the each directory one by one (customer, product, gateway), by using this command
    ```bash
    $ cd <dir>
    ```
2. Make sure you have maven installed on my computer, use `mvn -v` to check the version.
3. Setup your credential. You can configure it by creating file `env.properties` on the **root of the each service project (customer and product)**, aligned with pom.xml, then fill it with this format.
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

If all the instruction is well executed, The API Gateway will be executed on [localhost:8080](http://localhost:8080), Product Service will be on [localhost:8081](http://localhost:8081), and Customer Service will be on [localhost:8082](http://localhost:8082). Go check them out to see that the Cloud Gateway is now works.

### 🔑 List of Endpoints
#### 1. Product Service ([localhost:8081](http://localhost:8081))

Access the swagger [here](http://localhost:8081/swagger-ui/index.html)

![Screenshots](/Week%2010/Lecture%2017/Assignment%2001/img/product.png)

#### 2. Customer Service ([localhost:8082](http://localhost:8082))

Access the swagger [here](http://localhost:8082/swagger-ui/index.html)

![Screenshots](/Week%2010/Lecture%2017/Assignment%2001/img/customer.png)


### 🚀 Demonstration
#### 1. Direct request to Product Service (`GET /v1/products/{productId}`)
![Screenshots](/Week%2010/Lecture%2017/Assignment%2001/img/dir-product.png)

The request is directed from API Gateway into the Product Service and then return the result back to the API Gateway.

#### 2. Direct request to Customer Service (`GET /v1/customers/{customerId}`)
![Screenshots](/Week%2010/Lecture%2017/Assignment%2001/img/dir-customer.png)

The request is directed from API Gateway into the Customer Service and then return the result back to the API Gateway.

#### 2. Request to Customer then Product Service (`GET /v1/customers/{customerId}/products`)
![Screenshots](/Week%2010/Lecture%2017/Assignment%2001/img/customer-product.png)

The request is directed from API Gateway into the Customer Service, then Customer Service call Product Service through WebClient, and then return the result back to the API Gateway.