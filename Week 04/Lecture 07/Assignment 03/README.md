# 👩🏻‍🏫 Lecture 07 - Spring Core
> This repository is created as a part of assignment for Lecture 07 - Spring Core

## 🌱 Assignment 03 - Dependency Injection and Bean Annotations (Part 3)
### 🎯 Task 1 - Add Bean Scopes (Singleton, Prototype) to Assignment 2
**Bean scopes** in Spring determine the lifecycle and visibility of beans within the Spring container. Here, we’ll focus on:
- **Singleton Scope**: A single instance of the bean is created and shared across the entire application.
- **Prototype Scope**: A new instance of the bean is created each time it is requested.

#### 👨‍💻 Implementation
1. [**`EmailServiceImpl` Interface**](/Week%2004/Lecture%2007/Assignment%2003/lecture_7/src/main/java/com/example/lecture_7/service/EmailServiceImpl.java)

    `EmailServiceImpl` is already a singleton by default, but we'll explicitly annotate it for clarity and print its hash code when the `sendEmail` method is called.

2. [**`EmployeeServiceConstructor` Class**](/Week%2004/Lecture%2007/Assignment%2003/lecture_7/src/main/java/com/example/lecture_7/service/EmployeeServiceConstructor.java)

    We'll modify the `EmployeeServiceConstructor` class to demonstrate the prototype scope.
    - The `@Scope("prototype")` annotation makes each request for `EmployeeServiceConstructor` create a new instance.
    - The `notifyEmployee` method will print the hash code of the current `EmployeeServiceConstructor` instance, demonstrating that different instances are created for each call.

3. [**`Lecture7Application` Class**](/Week%2004/Lecture%2007/Assignment%2003/lecture_7/src/main/java/com/example/lecture_7/Lecture7Application.java)

    We’ll print the hash codes to demonstrate that `EmailService` behaves as singleton and `EmployeeServiceConstructor` behaves as a prototype.

#### ⚙️ How to run the program
1. Go to the `lecture_7` directory by using this command
    ```bash
    $ cd lecture_7
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

If all the instruction is well executed, the result will be something like this.

![Screenshot](img/result.png)

#### 📝 Summary
1. **Singleton Scope**: Demonstrated by `EmailServiceImpl`, which is a singleton by default. The same instance is used across the application.
2. **Prototype Scope**: Demonstrated by `EmployeeServiceConstructor`, which creates a new instance each time it is requested.

This clear differentiation shows how different scopes manage bean instances, and printing hash codes helps in verifying the behavior.

---

### 🕹️ Task 2 - Create a Controller and Test Request Scope
We will create a simple Spring MVC controller that uses a **request-scoped** bean. We'll demonstrate how this scope works in the context of HTTP requests by printing out information about the bean instance for each request. This will illustrate that a new bean instance is created for each HTTP request.

#### 🤔 **Understanding Request Scope**
**Request Scope**: In the Spring context, a bean defined with the request scope will have a new instance created for each HTTP request. This is commonly used in web applications to handle user-specific data during the request processing.

#### 🐾 **Implementation Steps**

1. **Create a Request-Scoped Bean**: Define a bean that has the request scope.
2. **Create a Controller**: Use this bean in a Spring MVC controller to demonstrate its behavior.
3. **Test the Request Scope**: Use a simple client or browser to make HTTP requests and observe the bean's behavior.

#### 👨‍💻 **Implementation**

1. **`RequestScopedBean.java`**(Request-Scoped Bean)

    We’ll define a bean that will be instantiated for each HTTP request. We’ll print its hash code to verify that a new instance is created for each request.
    
    The code is implemented on [this file](/lecture_7_1/src/main/java/com/example/lecture_7_1/controller/RequestController.java)

    Here is the explanation on what the code actually done.
    - The `@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)` annotation inject a proxy into your controller instead of the actual bean, allowing proper request scope resolution.
    - The `handleRequest` method prints the hash code of the instance, helping us verify that a new instance is created for each request.

2. **`RequestController.java`** (Controller)

    Create a controller that will use the `RequestScopedBean` to handle HTTP requests.

    The code is implemented on [this file](/lecture_7_1/src/main/java/com/example/lecture_7_1/service/RequestScopedBean.java)

    Here is the explanation on what the code actually done.
    - The `@RestController` annotation makes this class a REST controller that can handle HTTP requests.
    - The `@GetMapping("/testRequest")` annotation maps HTTP GET requests to the `testRequestScope` method.
    - This method calls `handleRequest` on `RequestScopedBean` and returns a message, while the `RequestScopedBean` prints its hash code to the console.
    - `ObjectFactory` from Spring’s `BeanFactory` is used to lazily resolve the request-scoped bean.

3. **`Lecture71Application.java`** (Main Application)

    This class is used as the main program. In this case we need to ensure your application is configured to scan for the `controller` and `service` packages.

    The code is implemented on [this file](/lecture_7_1/src/main/java/com/example/lecture_7_1/Lecture71Application.java)

    Here is the explanation on what the code actually done.
    - Ensure `@SpringBootApplication` is used to automatically configure and scan components.
    - The application will now handle HTTP requests to `/testRequest`.

#### ⚙️ **Testing Request Scope**

1. **Run the Application**: Start the Spring Boot application.
    1. Go to the `lecture_7_1` directory by using this command
        ```bash
        $ cd lecture_7_1
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

2. **Access the Endpoint**: Use a browser or a tool like `curl` or Postman to access `http://localhost:8080/testRequest`. In this section i use the curl one with commdn like this
    ```bash
    $ curl http://localhost:8080/testRequest
    ```

If you are doing well, the result will be something like this.

![Screenshot](img/result2.png)

Each request to `/testRequest` creates a new instance of `RequestScopedBean`, verified by different hash codes.

#### 📝 **Summary**

1. **Created `RequestScopedBean`**:
   - Defined with request scope, so a new instance is created for each HTTP request.
   - Prints its hash code to the console to verify instance creation.

2. **Created `RequestController`**:
   - Handles HTTP requests and uses `RequestScopedBean` to demonstrate the request scope.
   - Maps to the `/testRequest` endpoint.

3. **Tested the Request Scope**:
   - Verified new instances for each HTTP request using hash codes.

This setup effectively demonstrates the request scope in a Spring application, where each HTTP request receives a new bean instance, useful for handling request-specific data or operations.

---

### ❓ Task 3 - How to Inject Prototype Bean into Singleton Bean?
First, we need to understand a common issue in dependency injection within the Spring Framework and how to resolve it.

#### ❗ **Problem Overview**

- **Prototype Scope**: Each time a prototype-scoped bean is requested, a new instance is created.
- **Singleton Scope**: A single instance of the bean is created and shared throughout the application's lifecycle.

When a prototype bean is injected into a singleton bean directly, only a single instance of the prototype bean is created and shared within the singleton bean. This defeats the purpose of the prototype scope.

To solve this issue, there are several methods to ensure that a new instance of the prototype bean is created each time it is needed within a singleton bean:

1. **Using `ObjectFactory` or `Provider`**: The simplest approach using Spring’s built-in `ObjectFactory` or Java’s `Provider` from `javax.inject`.
2. **Using `@Lookup` Method Injection**: A method annotated with `@Lookup` will be overridden by the container to return a new instance of a prototype bean.
3. **Using Application Context**: Manually retrieving the bean from the application context.

#### 1️⃣ **1. Using `ObjectFactory` or `Provider`**

**Implementation Using `ObjectFactory`**

**Create `PrototypeBean`**

```java
package com.example.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PrototypeBean {
    public void doSomething() {
        System.out.println("PrototypeBean instance hash: " + this.hashCode());
    }
}
```

**Modify `SingletonBean`**

```java
package com.example.service;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SingletonBean {
    private final ObjectFactory<PrototypeBean> prototypeBeanFactory;

    @Autowired
    public SingletonBean(ObjectFactory<PrototypeBean> prototypeBeanFactory) {
        this.prototypeBeanFactory = prototypeBeanFactory;
    }

    public void usePrototypeBean() {
        PrototypeBean prototypeBean = prototypeBeanFactory.getObject();
        prototypeBean.doSomething();
    }
}
```

`ObjectFactory<PrototypeBean>` is used to lazily resolve a new instance of `PrototypeBean` each time it’s called.

#### 2️⃣ **2. Using `@Lookup` Method Injection**

**Modify `SingletonBean`**

```java
package com.example.service;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class SingletonBean {

    @Lookup
    public PrototypeBean getPrototypeBean() {
        // Spring will override this method to return a new PrototypeBean instance
        return null;
    }

    public void usePrototypeBean() {
        PrototypeBean prototypeBean = getPrototypeBean();
        prototypeBean.doSomething();
    }
}
```

`@Lookup` annotation tells Spring to override the method to return a new instance of `PrototypeBean`.

#### 3️⃣ **3. Using Application Context**

**Modify `SingletonBean`**

```java
package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SingletonBean {

    private final ApplicationContext applicationContext;

    @Autowired
    public SingletonBean(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void usePrototypeBean() {
        PrototypeBean prototypeBean = applicationContext.getBean(PrototypeBean.class);
        prototypeBean.doSomething();
    }
}
```

`applicationContext.getBean(PrototypeBean.class)` retrieves a new instance of `PrototypeBean` each time it’s called.

**Usage Example**

```java
package com.example.controller;

import com.example.service.SingletonBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingletonController {

    private final SingletonBean singletonBean;

    @Autowired
    public SingletonController(SingletonBean singletonBean) {
        this.singletonBean = singletonBean;
    }

    @GetMapping("/usePrototype")
    public String usePrototype() {
        singletonBean.usePrototypeBean();
        return "Check the console for the PrototypeBean instance hash code.";
    }
}
```

#### 📝 **Summary**

- **Using `ObjectFactory` or `Provider`**: Provides a factory to create new instances when needed, ensuring prototype behavior within a singleton.
- **Using `@Lookup` Method Injection**: Uses Spring’s method injection to return new prototype instances each time the method is called.
- **Using Application Context**: Retrieves prototype beans directly from the application context, creating a new instance each time.

---

### 🤷🏻‍♂️ Task 4 - Difference between `BeanFactory` and `ApplicationContext`

#### ✨ **Overview**

- **`BeanFactory`**: The root interface for accessing the Spring container. It provides basic functionality to manage beans.
- **`ApplicationContext`**: An extension of `BeanFactory` that adds more enterprise-specific functionality, including event propagation, declarative mechanisms to create a bean, and a more extensive means to work with the container.

#### 🔍 **Detailed Differences**
| Feature                            | `BeanFactory`                          | `ApplicationContext`                      |
|------------------------------------|----------------------------------------|-------------------------------------------|
| Basic Dependency Injection Container                 | Provides the basic mechanism to manage beans. You typically use `getBean` to retrieve beans, and it only instantiates a bean when it’s requested.                                    | Provides all the features of `BeanFactory` and more. It initializes all singleton beans at startup by default, which can be overridden.                                       |
| Bean Instantiation on Demand       | Yes                                    | No (Eager Initialization by default)      |
| Event Propagation                  | Does not support event propagation.                                     | Supports event propagation, allowing beans to publish and listen to application events.                                       |
| Internationalization (i18n)        | Does not support internationalization directly.                                     | Provides support for internationalization (i18n), allowing messages to be resolved in different locales.                                       |
| ApplicationContext Aware Beans     | Does not support `ApplicationContext` aware beans.                                     | Supports `ApplicationContext` aware beans, allowing them to access the `ApplicationContext` itself.                                       |
| Autowiring                         | Yes                                    | Yes                                       |
| BeanPostProcessor Support          | Limited support for `BeanPostProcessor`.                                | Full support for `BeanPostProcessor`, allowing for custom modifications of new bean instances.                                       |
| Built-in Bean Scopes               | Singleton, Prototype                   | Singleton, Prototype, Request, Session    |
| Integration with Web Applications  | Basic support for managing beans, not designed for web integration.                                | Rich support for web applications, with features like `WebApplicationContext`, session-scoped beans, and request-scoped beans.                              |
| Environment Abstraction            | Does not provide environment abstraction.                                     | Provides environment abstraction, allowing you to manage profiles, properties, and other environment configurations.                                       |
| Lifecycle Management               | Provides basic lifecycle management with `InitializingBean` and `DisposableBean`.                                 | Offers full lifecycle management with additional support for custom lifecycle events and integration with `ApplicationListener`.                                      |

### 📝 **Summary**

- **`BeanFactory`** is lightweight, primarily used for simple DI and is the root interface.
- **`ApplicationContext`** extends `BeanFactory`, providing more advanced and enterprise-specific features, including event handling, i18n, lifecycle management, and integration with web applications.