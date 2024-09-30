# 👩🏻‍🏫 Lecture 08 - Spring Boot
> This repository is created as a part of assignment for Lecture 08 - Spring Boot

## ✍ Assignment 01 - Create Spring Boot Project
### 🎯 **Create a Spring Boot Project**

In this case i'm using Spring Initializr to init my Spring Boot project. Here is how i initialize my Spring Boot project.

1. **Visit Spring Initializr:** Go to [start.spring.io](https://start.spring.io).

2. **Configure the Project:**
   - **Project:** Select `Maven Project` or `Gradle Project` (choose Maven in this case).
   - **Language:** Choose `Java`.
   - **Spring Boot:** Select the latest stable version (i choose 3.3.1 in this case).
   - **Project Metadata:**
     - **Group:** e.g., `com.example`
     - **Artifact:** e.g., `lecture_8_1`
     - **Name:** e.g., `lecture_8_1`
     - **Description:** e.g., `Demo project for Spring Boot`
     - **Package name:** e.g., `com.example.lecture_8_1`
     - **Packaging:** Choose `Jar`.
     - **Java Version:** Choose the appropriate version (i choose `21` in this case).

3. **Add Dependencies:** Add the necessary dependencies for our project. Common ones include:
   - `Spring Web` for web applications.
   - `Spring Data JPA` for data access.
   - `H2 Database` for an in-memory database (optional).
   - `Thymeleaf` for templating (optional).
   - `Spring Boot DevTools` for live reload (optional).

4. **Generate the Project:** Click `Generate` to download a ZIP file.

5. **Extract the ZIP:** Unzip the downloaded file to the preferred directory.

The project structure must be looking something like this.
```bash
lecture_8_1
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/lecture_8_1/
│   │   ├── DemoController.java
│   │   └── Lecture81Application.java
│   └── resources/
│       └── application.properties
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

### ⚙️ **Run the Spring Boot Application Locally**

In this case i'm using maven to run my project. Here is how i do that.

1. **Open Terminal:** 
    
    Navigate to the root directory of the project where the `pom.xml` file is located.

2. **Run the Application:**
    
    Execute the following command:
    ```bash
    $ ./mvnw spring-boot:run
    ```

3. **Access the Application:** 

    Once the application starts, we can access it typically at [http://localhost:8080](http://localhost:8080).

### 🚀 **Verify the Application**

Open the browser and navigate to [http://localhost:8080](http://localhost:8080) to see if our Spring Boot application is running.

Here’s a simple code i create to start a Spring Boot application.

[**`Lecture81Application.java`**](/Week%2004/Lecture%2008/Assignment%2001/lecture_8_1/src/main/java/com/example/lecture_8_1/Lecture81Application.java)
```java
package com.example.lecture_8_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lecture81Application {
	public static void main(String[] args) {
		SpringApplication.run(Lecture81Application.class, args);
	}
}
```

**`DemoController.java`**
```java
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }
}
```

In this example, when we navigate to [http://localhost:8080](http://localhost:8080), we should see "Hello, World!" displayed.

Here is the result showed.

![Screenshot](img/demo.png)