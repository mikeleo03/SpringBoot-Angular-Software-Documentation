# 👩🏻‍🏫 Lecture 07 - Spring Core
> This repository is created as a part of assignment for Lecture 07 - Spring Core

## 🌱 Assignment 02 - Dependency Injection and Bean Annotations (Part 2)
### ✍️ Task 1 - Working with Annotations
**Objective**: Create `EmailService` interface and `EmailServiceImpl` class. Add a method for sending email. Create `EmployeeService` class and use `EmailService` with Dependency Injection (DI) to send emails to employees about their work. Demonstrate DI using constructor, field, and setter injection.

#### 💬 Dependency Injection Methods
1. **Constructor Injection**: This method injects dependencies through the class constructor. It’s often preferred for its simplicity and for making dependencies explicit and immutable.
2. **Field Injection**: This method injects dependencies directly into the fields of the class. It’s less preferred because it hides dependencies and makes testing harder.
3. **Setter Injection**: This method injects dependencies through setter methods. It’s useful for optional dependencies or where dependencies can change during the object's lifecycle.

#### 👨‍💻 Implementation
1. [**`EmailService` Interface**](/Week%2004/Lecture%2007/Assignment%2002/lecture_7/src/main/java/com/example/lecture_7/service/EmailService.java)
    - **Purpose**: Define a contract for email sending functionality.
    - **Methods**: Should at least include `sendEmail`.
2. [**`EmailServiceImpl` Class**](/Week%2004/Lecture%2007/Assignment%2002/lecture_7/src/main/java/com/example/lecture_7/service/EmailServiceImpl.java)
    - **Purpose**: Implement the `EmailService` to provide actual email sending logic.
    - **Annotations**: Use `@Service` to denote it as a Spring-managed service component.
3. [**`EmployeeServiceConstructor` Class**](/Week%2004/Lecture%2007/Assignment%2002/lecture_7/src/main/java/com/example/lecture_7/service/EmployeeServiceConstructor.java)

    The `EmailService` is injected via the constructor. This ensures that `EmployeeServiceConstructor` is properly instantiated with a valid `EmailService`.
    - **Purpose**: Use `EmailService` to send notifications to employees.
    - **Tasks**: Demonstrate DI through constructor injection.
    - **Preferred**: Dependencies are immutable, making the class easier to test and understand.
4. [**`EmployeeServiceField` Class**](/Week%2004/Lecture%2007/Assignment%2002/lecture_7/src/main/java/com/example/lecture_7/service/EmployeeServiceField.java)
    
    The `@Autowired` annotation directly injects the `EmailService` into the field. This can make it less clear what dependencies the class needs.
    - **Purpose**: Use `EmailService` to send notifications to employees.
    - **Tasks**: Demonstrate DI through field injection.
    - **Direct Injection**: Dependencies are directly injected into fields.
    - **Less Preferred**: Less explicit, and harder to test as it requires Spring’s context to be loaded.
5. [**`EmployeeServiceSetter` Class**](/Week%2004/Lecture%2007/Assignment%2002/lecture_7/src/main/java/com/example/lecture_7/service/EmployeeServiceSetter.java)
    
    The `EmailService` is injected through a setter method. This allows flexibility in changing the `EmailService` dependency if needed.
    - **Purpose**: Use `EmailService` to send notifications to employees.
    - **Tasks**: Demonstrate DI through field injection.
    - **Configurable**: Dependencies can be set or changed after object creation.
    - **Use Case**: When dependencies are optional or can be changed.
6. [**`AppConfig` Class**](/Week%2004/Lecture%2007/Assignment%2002/lecture_7/src/main/java/com/example/lecture_7/config/AppConfig.java)
    - **`@Configuration`**: This annotation is used to indicate that the class declares one or more `@Bean` methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime.
    - **`@ComponentScan(basePackages = "com.example.lecture_7")`**: This annotation is used to specify the base packages to scan for Spring components. In this case, it tells Spring to scan the `com.example.lecture_7` package and its sub-packages for Spring components such as `@Component`, `@Service`, `@Repository`, and `@Controller`. Spring will then automatically register these components as beans in the application context.
    - **Purpose**: `AppConfig` serves as a configuration class for the Spring application. It typically includes configuration of beans using `@Bean` methods, setup of Spring-specific configurations, and enabling component scanning to discover Spring components automatically.
7. [**`Lecture7Application` Class**](/Week%2004/Lecture%2007/Assignment%2002/lecture_7/src/main/java/com/example/lecture_7/Lecture7Application.java)
    Serves as the main application for the Java Spring Boot.

#### 👀 Testing
To test the `EmployeeService` classes and their interaction with `EmailService` in a Spring Boot application, we can place your test code in the `src/test/java` directory. This is the standard convention for Java testing in Spring Boot projects.

The test is defined in [**`EmployeeServiceConstructorTest`**](/Week%2004/Lecture%2007/Assignment%2002/lecture_7/src/test/java/com/example/lecture_7/service/EmployeeServiceConstructorTest.java), [**`EmployeeServiceFieldTest`**](/Week%2004/Lecture%2007/Assignment%2002/lecture_7/src/test/java/com/example/lecture_7/service/EmployeeServiceFieldTest.java), and [**`EmployeeServiceSetterTest`**](/Week%2004/Lecture%2007/Assignment%2002/lecture_7/src/test/java/com/example/lecture_7/service/EmployeeServiceSetterTest.java) classes.

Here is how to run all the tests
1. Go to the `lecture_7` directory by using this command
    ```bash
    $ cd lecture_7
    ```
2. Make sure you have maven installed on your computer, use `mvn -v` to check the version.
3. Run this code to do the test
    ```bash
    $ mvn test
    ```

If all the instruction is well executed, the test result will be something like this.

![Screenshot](img/result.png)

From the result, we know that all the test **executed successfully** and all of them **give the same output**.