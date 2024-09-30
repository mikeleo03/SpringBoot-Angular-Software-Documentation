# 👨🏻‍🏫 Lecture 14 - Spring Advanced Features: FeignClient, RestTemplate, WebClient

> This repository is created as a part of an assignment for Lecture 14 on Spring Advanced Features: FeignClient, RestTemplate, WebClient.

## 💡 Assignment 01 - FeignClient, RestTemplate, WebClient

### 🧐 What are FeignClient, RestTemplate, and WebClient?

Spring offers several tools for making HTTP requests to external services or APIs. Each tool has its own strengths and is suited for different use cases:

- **FeignClient**: A declarative web service client that simplifies HTTP API calls by reducing the amount of boilerplate code required. It integrates seamlessly with Spring Cloud for easy integration with microservices.

  **Syntax Example**:
  ```java
  @FeignClient(name = "inventory-service", url = "http://inventory-service")
  public interface InventoryClient {
      
      @GetMapping("/inventory/{productId}")
      InventoryResponse getInventory(@PathVariable("productId") String productId);
  }
  ```

- **RestTemplate**: A synchronous HTTP client that provides a straightforward way to consume RESTful services. It's been a staple in Spring for many years, known for its simplicity and ease of use.

  **Syntax Example**:
  ```java
  @Bean
  public RestTemplate restTemplate() {
      return new RestTemplate();
  }

  // Usage
  ResponseEntity<String> response = restTemplate.getForEntity("http://example.com/api/resource", String.class);
  ```

- **WebClient**: A modern, reactive, and non-blocking HTTP client designed to work well with the Spring WebFlux framework. It supports asynchronous and streaming scenarios, making it ideal for reactive programming.

  **Syntax Example**:
  ```java
  @Bean
  public WebClient webClient() {
      return WebClient.builder().baseUrl("http://example.com").build();
  }

  // Usage
  Mono<String> response = webClient.get()
                                   .uri("/api/resource")
                                   .retrieve()
                                   .bodyToMono(String.class);
  ```

### ❓ What are the Differences?

Understanding the differences between FeignClient, RestTemplate, and WebClient helps in choosing the right tool for your specific needs:

- **Declarative vs. Programmatic**:
  - **FeignClient** is declarative, allowing you to define API clients using interfaces.
  - **RestTemplate** and **WebClient** require more programmatic control but offer greater flexibility.

- **Synchronous vs. Asynchronous**:
  - **FeignClient** and **RestTemplate** are synchronous, meaning they block the calling thread until the response is received.
  - **WebClient** supports both synchronous and asynchronous operations, making it suitable for reactive and non-blocking scenarios.

- **Reactive Support**:
  - **WebClient** is designed with reactive programming in mind and works seamlessly with Project Reactor and Spring WebFlux.
  - **FeignClient** and **RestTemplate** are more traditional and do not support reactive paradigms.

Here's a table summarizing these differences:

| Feature           | FeignClient                                    | RestTemplate                                   | WebClient                                       |
|-------------------|------------------------------------------------|------------------------------------------------|-------------------------------------------------|
| **Declarative API** | Yes                                            | No                                             | Yes                                             |
| **Asynchronous**  | No                                             | No                                             | Yes                                             |
| **Reactive Support** | No                                             | No                                             | Yes                                             |
| **HTTP Methods**   | Limited (GET, POST, etc.)                      | Full control                                   | Full control                                    |

### 🤔 Use Cases

- **FeignClient**: Ideal for microservices architectures where you need to make simple, declarative HTTP requests. For example, when one microservice needs to call another microservice, FeignClient provides a clean and easy-to-understand interface.

  **Real Case**: In a microservices setup for an e-commerce application, a `PaymentService` might need to call an external `InventoryService` to check stock levels. Using FeignClient simplifies this inter-service communication.

- **RestTemplate**: Best suited for straightforward, synchronous HTTP operations. It's often used in scenarios where you need to make blocking calls to RESTful services and handle simple request-response cycles.

  **Real Case**: When building a server-side application that needs to fetch data from an external weather API and display it on a dashboard, RestTemplate offers a simple way to make these HTTP calls and process the responses.

- **WebClient**: Perfect for handling asynchronous and streaming scenarios, especially in reactive applications. It is the go-to choice for non-blocking I/O operations and integrating with reactive streams.

  **Real Case**: In a real-time chat application, WebClient can be used to stream data from a messaging service, allowing for asynchronous communication and real-time updates without blocking the main thread.

### 🗂️ List of Projects

To provide clear examples and explanations, each project is organized separately:

1. **FeignClient Demo**
   - **Directory**: [feignClientDemo](/Week%2008/Lecture%2014/Assignment%2001/FeignClientDemo/feignClientDemo)
   - **Inspiration**: [Baeldung FeignClient Guide](https://www.baeldung.com/spring-cloud-openfeign​)
   - **Description**: Demonstrates how to use FeignClient to make HTTP requests using a declarative approach. Includes examples of GET and POST requests.
   
   **Key Points**:
   - Declarative API definition
   - Simplified microservices communication
   - Integration with Spring Cloud

2. **RestTemplate Demo**
   - **Directories**:
     - [RestTemplateDemo](/Week%2008/Lecture%2014/Assignment%2001/RestTemplateDemo/restTemplateDemo)
     - [RestTemplateDemo1](/Week%2008/Lecture%2014/Assignment%2001/RestTemplateDemo/restTemplateDemo1)
   - **Inspiration**: [Baeldung RestTemplate Guide](https://www.baeldung.com/rest-template​) and [Handling Lists with RestTemplate](https://www.baeldung.com/resttemplate-list​​)
   - **Description**: Provides examples of how to use RestTemplate for synchronous HTTP requests. Includes scenarios for GET, POST, PUT, and DELETE operations, as well as handling complex responses.

   **Key Points**:
   - Synchronous HTTP operations
   - Handling various HTTP methods
   - Processing complex responses

3. **WebClient Demo**
   - **Directory**: [webClientDemo](/Week%2008/Lecture%2014/Assignment%2001/WebClientDemo/webClientDemo)
   - **Inspiration**: [Baeldung WebClient Guide](https://www.baeldung.com/spring-webclient-json-list​)
   - **Description**: Showcases the capabilities of WebClient for asynchronous and reactive programming. Demonstrates how to handle various HTTP methods and stream data efficiently.

   **Key Points**:
   - Asynchronous and reactive programming
   - Streaming data handling
   - Integration with Spring WebFlux

### ⚙️ How to run the program

1. Navigate to the desired directory. For example, to run the WebClient demo:
   ```bash
   $ cd WebClientDemo/webClientDemo
   ```

2. Ensure Maven is installed. Check the version with:
   ```bash
   $ mvn -v
   ```

3. Execute the unit tests to verify functionalities:
   ```bash
   $ mvn test
   ```

### 🚀 Demonstration

Here are the results from demonstrating all functionalities in the WebClientDemo project:

![Demo](/Week%2008/Lecture%2014/Assignment%2001/img/demo.png)