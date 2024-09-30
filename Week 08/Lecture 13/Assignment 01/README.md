# 👨🏻‍🏫 Lecture 13 - Spring Advance Feature: Filter and Spring Interceptor
> This repository is created as a part of assignment for Lecture 13 - Spring Advance Feature: Filter and Spring Interceptor

## 🔍 Assignment 01 - Research about `onceperrequestfilter`

### 🧐 Detailed Overview

#### **What is a Filter?**
In the context of a Java web application, a filter is a component that performs tasks before or after a request is processed by a servlet. Filters can modify request and response objects, and they are useful for cross-cutting concerns like logging, authentication, and response modification.

#### **Purpose of `OncePerRequestFilter`**
The `OncePerRequestFilter` class is designed to handle cases where filters might be executed multiple times for a single request due to forwarding or including requests. This ensures that a filter’s logic is only executed once per request, preventing redundant processing and potential performance issues.

#### **How `OncePerRequestFilter` Works**

1. **Filter Lifecycle**
   
   Filters in a web application are managed by the servlet container (e.g., Tomcat). The `OncePerRequestFilter` ensures that the `doFilterInternal` method is called only once per request.

2. **Request Wrapping**
   
   When a request is forwarded or included, it might be wrapped in additional `ServletRequest` objects. `OncePerRequestFilter` ensures that the filtering logic is applied only once, even if the request has been wrapped multiple times.

3. **Thread Safety**
   
   The `OncePerRequestFilter` class is designed to be thread-safe, meaning that its instance can be safely used across multiple threads handling different requests.

### 👨🏻‍💻 **Advanced Example: Authentication Filter**

Let’s create an advanced example of a filter that checks for a specific header in requests to enforce custom authentication. This example will include detailed aspects, such as handling exceptions and configuring the filter in a Spring Boot application.

#### **Authentication Filter Example**

1. **Create the Filter Class**

    ```java
    import org.springframework.web.filter.OncePerRequestFilter;

    import javax.servlet.FilterChain;
    import javax.servlet.FilterConfig;
    import javax.servlet.ServletException;
    import javax.servlet.ServletRequest;
    import javax.servlet.ServletResponse;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;

    public class CustomAuthenticationFilter extends OncePerRequestFilter {

        private static final String AUTH_HEADER = "X-Custom-Auth";

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            // Check for the custom authentication header
            String authHeader = request.getHeader(AUTH_HEADER);
            if (authHeader == null || !authHeader.equals("expectedValue")) {
                // If the header is missing or incorrect, respond with an unauthorized status
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            // Continue the request-response chain if authentication is successful
            filterChain.doFilter(request, response);
        }
    }
    ```

2. **Register the Filter in Spring Boot Configuration**

    In Spring Boot, we can configure the filter either by using `FilterRegistrationBean` or by annotating a configuration class.

    ```java
    import org.springframework.boot.web.servlet.FilterRegistrationBean;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class FilterConfig {

        @Bean
        public FilterRegistrationBean<CustomAuthenticationFilter> customAuthenticationFilter() {
            FilterRegistrationBean<CustomAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new CustomAuthenticationFilter());
            registrationBean.addUrlPatterns("/api/*"); // Apply filter to specific URL patterns
            return registrationBean;
        }
    }
    ```

### 🚀 **Explanation**
Here is the explanation on what i already made on the previous segment.

1. **Filter Logic (`doFilterInternal` Method)**
   - **Header Check**: The filter checks if the custom header `X-Custom-Auth` is present and has the expected value.
   - **Unauthorized Response**: If the header is missing or incorrect, it sends a `401 Unauthorized` response and halts further processing.
   - **Continue Chain**: If the header is valid, the request is passed down the filter chain.

2. **Filter Registration**
   - **FilterRegistrationBean**: This bean registers the filter with the Spring context.
   - **addUrlPatterns("/api/*")**: Specifies that the filter should be applied to URLs that start with `/api/`.

### 🔑 **Key Points**

1. **Execution Control**: `OncePerRequestFilter` ensures the filter logic is applied once per request even if the request is forwarded or included multiple times.

2. **Thread Safety**: You should be cautious about mutable state within your filter. Since filters are often accessed by multiple threads, any mutable state should be handled carefully.

3. **Exception Handling**: It’s essential to handle exceptions gracefully within filters, especially when dealing with authentication or authorization, to avoid exposing sensitive information.

4. **Configuration**: Filters can be configured to apply to specific URL patterns or to all requests, depending on your needs.