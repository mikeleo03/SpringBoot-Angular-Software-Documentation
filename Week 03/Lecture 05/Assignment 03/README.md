# 👨🏻‍🏫 Lecture 05 - Basic Backend, Spring
> This repository is created as a part of assignment for Lecture 05 - Basic Backend, Spring

## 🔍 Assignment 03 - Comparison of Restful API Best Practice
### 1️⃣ Accept and Respond with JSON

#### Best Practices
- **JSON Handling:** REST APIs should accept JSON payloads for requests and respond with JSON, as it's a widely accepted data interchange format.
- **Content Negotiation:** APIs should support `Content-Type` and `Accept` headers to handle JSON.

#### Assignment 2 Implementation
- **Compliance:** The code uses `@RestController` to ensure JSON responses. Methods annotated with `@PostMapping` and `@PutMapping` consume JSON payloads using `@RequestBody`.
- **Response Handling:** `ResponseEntity` is used to configure and return JSON responses.

#### Differences
- **Full Compliance:** The implementation adheres to the JSON handling best practices effectively.

### 2️⃣ Use Nouns in URIs

#### Best Practices
- **Naming Conventions:** Use nouns to represent resources in endpoint paths, as HTTP methods already define actions.

#### Assignment 2 Implementation
- **Correct Usage:** Most paths, such as `@RequestMapping("/api/v1/employee")`, correctly use nouns.
- **Non-compliance:** Endpoints like `"/upload-csv"` use verbs, which is not recommended.

#### Differences
- **Path Naming:** While the majority of the endpoints use nouns, some deviate by including verbs, which should be revised to adhere to best practices (e.g., `"/employee/upload"`).

### 3️⃣ Use Logical Nesting on Endpoints

#### Best Practices
- **Hierarchical Grouping:** Endpoints should reflect the relationships between resources, using nested paths where appropriate.

#### Assignment 2 Implementation
- **Basic Grouping:** CRUD operations are grouped under `/api/v1/employee`, representing a common base path.
- **No Further Nesting:** Additional relationships (e.g., departments under employees) are not nested.

#### Differences
- **Hierarchical Design:** While basic grouping is used, more complex relationships are not reflected in the path structure. Implementing nested paths like `/api/v1/employees/{employeeId}/departments` could improve clarity.

### 4️⃣ Handle Errors Gracefully

#### Best Practices
- **Error Handling:** APIs should handle errors gracefully and use standard HTTP status codes to convey error information.

#### Assignment 2 Implementation
- **Detailed Error Handling:** Responses include appropriate status codes (e.g., `204 No Content`, `404 Not Found`, `200 OK`, `400 Bad Request`, `500 Internal Server Error`).
- **Meaningful Responses:** Error messages provide context for the issues encountered.

#### Differences
- **Comprehensive Error Handling:** The implementation aligns well with best practices, handling various scenarios with appropriate error responses.

### 5️⃣ Provide Filtering, Sorting, and Pagination

#### Best Practices
- **Data Management:** APIs should support filtering, sorting, and pagination to handle large datasets efficiently.

#### Assignment 2 Implementation
- **Partial Implementation:** Filtering by department is available.
- **Missing Features:** Sorting and pagination are not implemented.

#### Differences
- **Enhancements Needed:** Implementing sorting (e.g., `?sort=name`) and pagination (e.g., `?page=2&limit=20`) would align the implementation more closely with best practices.

### 6️⃣ Maintain Good Security Practices

#### Best Practices
- **Encryption and Access Control:** Use SSL/TLS for encrypted communication and implement role-based access control.

#### Assignment 2 Implementation
- **Security Lacking:** SSL/TLS is not used, and there are no role checks implemented.

#### Differences
- **Security Improvements:** Adding SSL/TLS and role-based access checks (e.g., using Spring Security) is necessary to meet best practices.

### 7️⃣ Cache Data to Improve Performance

#### Best Practices
- **Caching:** Implement caching to reduce load on the database and improve response times, while managing the risk of serving stale data.

#### Assignment 2 Implementation
- **No Caching:** Caching is not implemented, leading to potential performance issues.

#### Differences
- **Performance Enhancements:** Adding caching mechanisms (e.g., using Redis or in-memory caching) could improve performance, especially for frequently accessed data.

### 8️⃣ Versioning APIs

#### Best Practices
- **API Versioning:** Use versioning to introduce changes without breaking existing clients, typically by including version numbers in the URL.

#### Assignment 2 Implementation
- **Versioning Implemented:** API versioning is in place with `v1`, as indicated by `@RequestMapping("/api/v1/employee")`.

##### Differences
- **Future-Proofing:** The current implementation adheres to best practices, allowing for future API changes without breaking existing integrations.

### 📝 Summary of Differences
- **Endpoint Naming:** Revise endpoints using verbs to use nouns.
- **Path Nesting:** Reflect more complex relationships through nested paths.
- **Filtering, Sorting, Pagination:** Add sorting and pagination to enhance data handling.
- **Security:** Implement SSL/TLS and role-based access control.
- **Caching:** Introduce caching to improve performance.

Overall, while Assignment 2 aligns well with many REST API best practices, it could be further enhanced by addressing these differences.