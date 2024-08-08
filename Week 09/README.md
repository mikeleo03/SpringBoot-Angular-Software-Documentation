# 👩🏻‍🏫 Lecture 15 & 16 - Unit Test and Code Quality Measurement
> This repository is created as a part of assignment for Lecture 15 & 16 - Unit Test and Code Quality Measurement

## 🚀 Assignment 01 - Write Unit Test with Coverage More Than 75%

### 🔎 What is Unit Test?
Unit testing involves testing individual components or functions of an application in isolation to ensure they work as expected. Unit tests are written to validate the behavior of a small, specific part of the code, usually a single method or function. By doing so, they help identify bugs early in the development cycle.

### 🤔 How to Do Unit Test in Java SpringBoot
1. **Set Up The Project** 
    
    Ensure that we have a Spring Boot project set up. We can use tools like Spring Initializr to generate the project.
2. **Add Testing Dependencies**
    
    Include dependencies for testing frameworks such as JUnit and Mockito in the `pom.xml` or `build.gradle` file.
3. **Create Test Classes**
    
    For each class we want to test, create a corresponding test class in the `src/test/java` directory.
4. **Write Test Methods**
    
    Use the `@Test` annotation to create test methods within the test classes.
5. **Mock Dependencies**
    
    Use Mockito to mock dependencies and isolate the unit being tested.
6. **Run Tests**
    
    Use the IDE or command line to run the tests and ensure they pass.

### 👣 Step-by-Step Explanation
1. **Create a Spring Boot Project**:
    ```bash
    spring init -dweb,data-jpa,h2,devtools --groupId=com.example --artifactId=demo --name=demo
    ```
2. **Add Dependencies in `pom.xml`**:
    ```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    ```
3. **Write a Unit Test**:
    Here is the example on how Unit Test is written.
    ```java
    @RunWith(SpringRunner.class)
    @SpringBootTest
    public class UserServiceTest {
        
        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private UserService userService;

        @Test
        public void whenValidUser_thenUserShouldBeFound() {
            User user = new User("John", "Doe");
            when(userRepository.findByName("John")).thenReturn(Optional.of(user));
            
            Optional<User> found = userService.findByName("John");
            assertTrue(found.isPresent());
            assertEquals(found.get().getName(), "John");
        }
    }
    ```

### 👨🏻‍💻 Implementation:
1. **Define The Service**:
    ```java
    @Service
    public class UserService {
        @Autowired
        private UserRepository userRepository;

        public Optional<User> findByName(String name) {
            return userRepository.findByName(name);
        }
    }
    ```
2. **Define The Repository**:
    ```java
    public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByName(String name);
    }
    ```
3. **Run The Tests** using the IDE or:
    ```bash
    mvn test
    ```

### 📝 Some Notable Mentions
- Ensure that tests are isolated and do not rely on each other.
- Use meaningful names for the test methods to describe what they are testing.
- Aim for high coverage, but remember quality over quantity.

## ⚡ Assignment 02 - SonarLint and SonarQube for Code Quality Testing

### 🔎 What is SonarLint and SonarQube?
- **SonarLint**: An IDE extension that provides on-the-fly feedback to developers on new bugs and quality issues injected into their code.
- **SonarQube**: A web-based platform for continuous inspection of code quality. It performs automatic reviews with static analysis of code to detect bugs, code smells, and security vulnerabilities.

### 👣 Step-by-Step Explanation
1. **Install SonarLint** in the IDE (e.g., IntelliJ, Eclipse, VSCode).
2. **Configure SonarQube**:
    - Install SonarQube on the local machine or use a hosted version.
    - Set up a project in SonarQube and generate a token.
    - Add the SonarQube properties to the `sonar-project.properties` file:
        ```properties
        sonar.projectKey=my-project
        sonar.projectName=my-project
        sonar.projectVersion=1.0
        sonar.host.url=http://localhost:9000
        ```
3. **Run SonarQube Analysis**:
    ```bash
    mvn sonar:sonar
    ```

### 📝 Some Notable Mentions
- Regularly analyze the code with SonarQube to maintain high code quality.
- Use SonarLint in the IDE to catch issues early in the development process.
- Address the issues reported by SonarQube to improve the maintainability and reliability of code.

## 🔥 Assignment 03 - JaCoCo for Determining Unit Test Coverage

### 🔎 What is JaCoCo?
JaCoCo (Java Code Coverage) is a free code coverage library for Java. It provides detailed information about code coverage, showing which parts of the code have been executed by tests and which parts have not.

### 👣 Step-by-Step Explanation
1. **Add JaCoCo Plugin to `pom.xml`**:
    ```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.7</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ```
2. **Run Tests with JaCoCo**:
    ```bash
    mvn clean test
    mvn jacoco:report
    ```
3. **View Coverage Report**: The report will be generated in the `target/site/jacoco/index.html` file. Open this file in a browser to view detailed coverage information.

### 📝 Some Notable Mentions
- Aim for high code coverage but focus on testing critical and complex parts of the application.
- Use JaCoCo reports to identify untested parts of the code and write additional tests for them.
- Integrate JaCoCo with the CI/CD pipeline to ensure code coverage is continuously monitored.

---

## ✅ Assignment Documentations and Results

### 💡 How to Set Up All the Requirements
1. **Install SonarQube**:
   Download the Community Edition from [this link](https://www.sonarsource.com/products/sonarqube/downloads/).
2. **Start SonarQube**:
   - Run `StartSonar.bat`.
   - If the instance fails to start, check the logs to find the cause.
3. **Log In to SonarQube**:
   - Access http://localhost:9000/ with System Administrator credentials (login=`admin`, password=`admin`) or your credential if you already set it up.
   - For more details, refer to the [installation documentation](https://docs.sonarsource.com/sonarqube/latest/setup-and-upgrade/install-the-server/introduction/).
4. **Set Up SonarQube Token**:
   Add the SonarQube token to your environment variables. For PowerShell, use the following commands:
     ```powershell
     # Set the environment variable
     $env:SONAR_TOKEN = "<your-sonar-token>"

     # Verify the environment variable
     $env:SONAR_TOKEN
     ```
5. **Install JaCoCo Plugins**:
   Add the necessary JaCoCo plugins to your project’s `pom.xml` and configure any required environment variables. You can use what i already do on my [`pom.xml`](/Week%2009/fpt_midterm_pos/pom.xml).

### ⚙️ How to Run All the Tests and Code Quality Analysis
1. **Navigate to Project Directory**:
   ```bash
   $ cd fpt_midterm_pos
   ```
2. **Check Maven Installation**:
   Ensure Maven is installed by running `mvn -v` to check the version.
3. **Run Tests, Check Coverage, and Get Report**:
   ```bash
   $ ./mvnw clean verify jacoco:report sonar:sonar
   ```

   If all instructions are correctly executed, open [localhost:9000](http://localhost:9000) to view the project report.

### 📸 Result Documentation
1. **Unit Tests Created**

    <blockquote align='center'>
    <h3>162 unit tests created</h3>
    </blockquote>

   ![Screenshot](/Week%2009/img/unit.png)
   
   All tests run successfully.

2. **Unit Tests Code Coverage**

    <blockquote align='center'>
    <h3>Achieved 88% overall code coverage via JaCoCo</h3>
    </blockquote>

   ![Screenshot](/Week%2009/img/jacoco.png)

3. **Code Quality Tests**

    <blockquote align='center'>
    <h3>Achieved Grade "A" and 0 issue across all aspects as per SonarQube analysis</h3>
    </blockquote>

   ![Screenshot](/Week%2009/img/sonar1.png)
   
   Grade "A" with **0 issues in every single aspects**, with **82.8% test coverage**.

   ![Screenshot](/Week%2009/img/sonar2.png)
   
   Full overall code result analysis.

----

# 🛒 [Revisit] Midterm Exam Project - Point of Sales
> This repository is created as a part of Midterm Exam Project by Group 3.

## ⚡ Requirements
1. All APIs Have Pagination
2. Create RESTful APIs using Best Practice
3. Using Validate Annotation/Function
4. All Entities Should have created_time, Updated_time
5. Customer Implementation
    1. Show List Customer Field(id, name, phone number)
    2. Add New Customer
    3. Edit Existing Customer
    4. Edit Status Existing Customer
6. Product Implementation
    1. Show List Product Field(id, name, price, status), Search by name, Sort by name or price
    2. Add New Product
    3. Edit Existing Product
    4. Edit Status Existing Product
    5. Add New Product by Excel File
7. Invoice Implementation
    1. Show List Invoice Field(id, invoice amount, customer name, invoice date) with Invoice Detail Field(Customer: id, name; Invoice: id, invoice amount, invoice date; List Product: quantity, price, amount), Search by name(like) or customer(id) or date or month, Sort by date or amount
    2. Add New Invoice
    3. Edit Existing Invoice
    4. Export Invoice Detail to PDF
    5. Create Revenue by day or month or year(Optional)
    6. Export Invoice Detail to Excel(Optional)

## 🌳 Project Structure
```bash
fpt_midterm_pos
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/fpt_midterm_pos/
│   │   ├── config/
│   │   │   └── WebConfig.java
│   │   ├── controller/
│   │   │   ├── CustomerController.java
│   │   │   ├── InvoiceController.java
│   │   │   └── ProductController.java
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   ├── Customer.java
│   │   │   │   ├── Invoice.java
│   │   │   │   ├── InvoiceDetail.java
│   │   │   │   ├── InvoiceDetailKey.java
│   │   │   │   ├── Product.java
│   │   │   │   └── Status.java
│   │   │   └── repository/
│   │   │       ├── CustomerRepository.java
│   │   │       ├── InvoiceDetailRepository.java
│   │   │       ├── InvoiceRepository.java
│   │   │       └── ProductRepository.java
│   │   ├── dto/
│   │   │   ├── CustomerDTO.java
│   │   │   ├── CustomerInvoiceDTO.java
│   │   │   ├── CustomerSaveDTO.java
│   │   │   ├── CustomerShowDTO.java
│   │   │   ├── InvoiceDetailDTO.java
│   │   │   ├── InvoiceDetailSaveDTO.java
│   │   │   ├── InvoiceDetailSearchCriteriaDTO.java
│   │   │   ├── InvoiceDTO.java
│   │   │   ├── InvoiceSaveDTO.java
│   │   │   ├── InvoiceSearchCriteriaDTO.java
│   │   │   ├── ProductDTO.java
│   │   │   ├── ProductSaveDTO.java
│   │   │   ├── ProductSearchCriteriaDTO.java
│   │   │   ├── ProductShowDTO.java
│   │   │   └── ReevenueShowDTO.java
│   │   ├── exception/
│   │   │   ├── BadRequestException.java
│   │   │   ├── DuplicateStatusException.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── ResourceNotFound.java
│   │   ├── mapper/
│   │   │   ├── CustomerMapper.java
│   │   │   ├── InvoiceDetailMapper.java
│   │   │   ├── InvoiceMapper.java
│   │   │   └── ProductMapper.java
│   │   ├── service/
│   │   │   ├── impl/
│   │   │   │   ├── CustomerServiceImpl.java
│   │   │   │   ├── InvoiceServiceImpl.java
│   │   │   │   └── ProductServiceImpl.java
│   │   │   ├── CustomerService.java
│   │   │   ├── InvoiceService.java
│   │   │   └── ProductService.java
│   │   ├── utils/
│   │   │   ├── DateUtils.java
│   │   │   ├── ExcelGenerator.java
│   │   │   ├── FileUtils.java
│   │   │   └── PDFGenerator.java
│   │   └── FptMidtermPosApplication.java
│   └── resources/
│       ├── templates/
│       │   └── invoice-template.html
│       ├── application.properties
│       ├── data.sql
│       ├── productSample.xlsx
│       └── sonar-project.properties
├── .gitignore
├── env.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
├── run.bat
└── run.sh
```

## 🔎 Class Diagram
Here is our Class Diagram. You can see it detail [here](https://lucid.app/lucidchart/b66fa0c2-f3c7-49e4-9873-633c505bda5c/edit?beaconFlowId=96FD403F7522998F&invitationId=inv_c0d2a63e-ac6d-4693-b6bd-b15393c9d14e&page=0_0#).

![Class](/Week%2009/img/class.png)


## 🧩 Entity Relationship Diagram (ERD) and SQL Query Data
Here is our ERD. You can see it detail [here](https://lucid.app/lucidchart/b66fa0c2-f3c7-49e4-9873-633c505bda5c/edit?beaconFlowId=96FD403F7522998F&invitationId=inv_c0d2a63e-ac6d-4693-b6bd-b15393c9d14e&page=0_0#).

![ERD](/Week%2009/img/erd.png)

And here is the SQL query to create the database, table, and instantiate some data.
```sql
-- Create the database
CREATE DATABASE fpt_midterm_pos;

-- Use the database
USE fpt_midterm_pos;

-- Initialize table with DDL
-- Create `Customer` table
CREATE TABLE Customer (
    ID VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP
);

-- Create `Product` table
CREATE TABLE Product (
    ID VARCHAR(36) PRIMARY KEY,    -- Use VARCHAR for UUIDs
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    status VARCHAR(50) NOT NULL,   -- Use VARCHAR instead of ENUM
    quantity INT,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP
);

-- Create `Invoice` table
CREATE TABLE Invoice (
    ID VARCHAR(36) PRIMARY KEY,
    amount INT NOT NULL,    -- Remove length specifier from INT
    date DATE NOT NULL,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP,
    customerId VARCHAR(36),
    FOREIGN KEY (customerId) REFERENCES Customer(ID)
);

-- Create `InvoiceDetails` table
CREATE TABLE InvoiceDetails (
    invoiceID VARCHAR(36),
    productID VARCHAR(36),
    quantity INT,
    productPrice INT,
    productName VARCHAR(255),
    amount INT,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP,
    PRIMARY KEY (invoiceID, productID),
    FOREIGN KEY (invoiceID) REFERENCES Invoice(ID),
    FOREIGN KEY (productID) REFERENCES Product(ID)
);
```

There are also query to insert some generated dummy data. All the MySQL queries is available on [this file](/fpt_midterm_pos/src/main/resources/data.sql). Here is the query to drop the database.
```sql
-- Drop the database
DROP DATABASE IF EXISTS fpt_midterm_pos;
```

In this test, we're also discovering safer way to save the credential. You can configure env.properties on the **root of the project** (aligned with [pom.xml](/fpt_midterm_pos/pom.xml)) with this format.
```java
DB_DATABASE=<your database url>
DB_USER=<your database username>
DB_PASSWORD=M<your database password>
PORT=<your preferred port number>
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

## ⚙️ How to run the program
1. Go to the `fpt_midterm_pos` directory by using this command
    ```bash
    $ cd fpt_midterm_pos
    ```
2. Make sure you have maven installed on my computer, use `mvn -v` to check the version.
3. If you are using windows, you can run the program by using this command.
    ```bash
    $ ./run.bat
    ```
    And if you are using Linux, you can run the program by using this command.
    ```bash
    $ chmod +x run.sh
    $ ./run.sh
    ```

If all the instruction is well executed, Open [localhost:8080](http://localhost:8080) to see that the REST APIs is now works.

## 🔑 List of Endpoints
| Endpoints                                                                                  | Method | Description                                                                                     |
|--------------------------------------------------------------------------------------------|:------:|-------------------------------------------------------------------------------------------------|
| /api/v1/products                                                                           | GET    | Retrieve all products with default pagination (page 1 with size 20 elements/page). Consider only active products. |
| /api/v1/products?page={X}&size={Y}                                                         | GET    | Retrieve all products with custom pagination (page X (0-based index) with size Y elements/page). Consider only active products. |
| /api/v1/products?name={A}                                                                  | GET    | Retrieve all products with name A. Consider only active products.                               |
| /api/v1/products?sortByName={P}                                                            | GET    | Retrieve all products sorted by name in order P. P is either asc or desc, default asc.          |
| /api/v1/products?sortByPrice={Q}                                                           | GET    | Retrieve all products sorted by price in order Q. Q is either asc or desc, default asc.         |
| /api/v1/products?minPrice={X}&maxPrice={Y}                                                 | GET    | Retrieve all products with a price between X and Y.                                             |
| /api/v1/products?name=ProductB&sortByPrice=desc&minPrice=200&page=2&size=10                | GET    | Retrieve all products with name “Product B” with price above 200, then sort it by price descending, and show the result with custom pagination (page 3 with size 10). Consider only active products. |
| /api/v1/products                                                                           | POST   | Create a new product. Validate POST request format.                                             |
| /api/v1/products/{id}                                                                      | PUT    | Update an existing product by product ID. Make sure the product ID exists.                      |
| /api/v1/products/active/{id}                                                               | PUT    | Activate the existing product by their product ID. Make sure the product ID exists and is currently inactive. |
| /api/v1/products/deactive/{id}                                                             | PUT    | Deactivate the existing product by their product ID. Make sure the product ID exists and is currently active. |
| /api/v1/products/upload                                                                    | POST   | Import list of products from Excel file from form data. Consider Excel format and data validation. |
| /api/v1/customers                                                                          | GET    | Retrieve all customers with default pagination (page 1 with size 20 elements/page). Consider only active customers. |
| /api/v1/customers?page={X}&size={Y}                                                        | GET    | Retrieve all customers with custom pagination (page X (0-based index) with size Y elements/page). Consider only active customers. |
| /api/v1/customers                                                                          | POST   | Create a new customer. Validate POST request format.                                            |
| /api/v1/customers/{id}                                                                     | PUT    | Update an existing customer by customer ID. Make sure the customer ID exists.                   |
| /api/v1/customers/active/{id}                                                              | PUT    | Activate the existing customer by their customer ID. Make sure the customer ID exists and is currently inactive. |
| /api/v1/customers/deactive/{id}                                                            | PUT    | Deactivate the existing customer by their customer ID. Make sure the customer ID exists and is currently active. |
| /api/v1/invoices                                                                           | GET    | Retrieve all invoices with default pagination (page 1 with size 20 elements/page).              |
| /api/v1/invoices?page={X}&size={Y}                                                         | GET    | Retrieve all invoices with custom pagination (page X (0-based index) with size Y elements/page). |
| /api/v1/invoices?customerName={A}                                                          | GET    | Retrieve all invoices from customer name A.                                                     |
| /api/v1/invoices?customerId={A}                                                            | GET    | Retrieve all invoices from customer ID A. Make sure customer ID exists.                         |
| /api/v1/invoices?startDate={X}&endDate={Y}                                                 | GET    | Retrieve all invoices from date X to date Y.                                                    |
| /api/v1/invoices?month={C}                                                                 | GET    | Retrieve all invoices on month C. Consider case insensitive.                                    |
| /api/v1/invoices?sortByDate={P}                                                            | GET    | Retrieve all invoices sorted by date in order P. P is either asc or desc, default asc.          |
| /api/v1/invoices?sortByAmount={Q}                                                          | GET    | Retrieve all invoices sorted by amount in order Q. Q is either asc or desc, default asc.        |
| /api/v1/invoices?customerId=1&month=July&sortByAmount=desc                                 | GET    | Retrieve all invoices from customer with ID 1 in July, sort it by amount ascending.             |
| /api/v1/invoices                                                                           | POST   | Create a new invoice. Validate POST request format.                                             |
| /api/v1/invoices/{id}                                                                      | PUT    | Update existing invoice. Has the same business flow as creating a new invoice. Make sure the invoice ID exists. Invoice can only be edited in 10 minutes from created time. |
| /api/v1/invoices/{id}/export                                                               | GET    | Export the invoice details data into PDF. Include all information on invoice details.           |
| /api/v1/invoices/excel?customerId={id}&month={month}&year={year}                           | GET    | Export the invoice details data into Excel with criteria filter. Include all information on invoice details. |
| /api/v1/invoices/revenue                                                                   | GET    | Create a report revenue invoice based on a given year, month, or day. Make sure the input filter value is between year, month, or day. |

## 📝 Full Documentation and Report
To get the full documentation and report on what we already make, please visit the document we already have [here](https://docs.google.com/document/d/13k0Ruc8sySpOKDno9zcgG8Uq9EcpTQm4mUqg1-gfvNA/edit?usp=sharing).

## 📬 API Documentation
For API documentation we're using Swagger, so you can see the detail of API documentation on [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) after running the program.

## 🙋🏻‍♂️ Contributors
<a href = "https://github.com/affandyfandy/MidtermExamGroup03/graphs/contributors">
  <img src = "https://contrib.rocks/image?repo=affandyfandy/MidtermExamGroup03"/>
</a>