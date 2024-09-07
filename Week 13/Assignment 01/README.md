# 👨🏻‍🏫 Lecture 25 - Spring Security
> This repository is created as a part of the assignment for Lecture 25 - Spring Security

## 🔐 Assignment 01 - Implementing OAuth with Spring Security

### 🧐 Detailed Overview
In this assignment, i have implemented OAuth 2.0 authentication with Spring Security in a Spring Boot application. The goal was to integrate Google OAuth for secure login and authorization, along with handling JWT tokens for user authentication.

### 🛠️ Implementation Details

1. **OAuth2 Configuration**:
   - Integrated Google OAuth 2.0 for authentication.
   - Configured Spring Security to use OAuth2 login and JWT token management.
   - Implemented custom success and error handlers for OAuth2 login.

2. **CORS Configuration**:
   - Set up CORS policies to allow cross-origin requests from specified domains.
   - Configured CORS to handle preflight requests and custom headers.

3. **Backend Services**:
   - Added endpoints for JWT token generation and user registration.
   - Implemented JWT token handling and validation mechanisms.

4. **Frontend Integration**:
   - Configured Angular to handle authentication and communicate with the backend.
   - Implemented login and user registration pages with proper routing and guards.

### 📚 Summary
This assignment demonstrates the integration of Google OAuth 2.0 with a Spring Boot backend and an Angular frontend. It covers the entire flow of user authentication, from login to token management, and ensures secure communication between the client and server.

---

### 🌳 Project Structure

#### 1. Spring Boot Backend Project
```bash
spring_oauth2
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/
│   ├── java/com/example/spring_oauth2/
│   │   ├── config/
│   │   │   ├── filter/
│   │   │   │   ├── ApiKeyFilter.java
│   │   │   │   └── JwtRequestFilter.java
│   │   │   ├── CustomAuthenticationSuccessHandler.java
│   │   │   ├── FilterConfig.java
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── AuthenticationController.java
│   │   │   └── OAuth2Controller.java
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   ├── Apikey.java
│   │   │   │   └── User.java
│   │   │   └── repository/
│   │   │       ├── ApiKeyRepository.java
│   │   │       └── UserRepository.java
│   │   ├── dto/
│   │   │   ├── JwtRequest.java
│   │   │   └── JwtResponse.java
│   │   ├── exception/
│   │   │   ├── AuthException.java
│   │   │   ├── BadRequestException.java
│   │   │   ├── DuplicateStatusException.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── ResourceNotFoundException.java
│   │   ├── service/
│   │   │   ├── impl/
│   │   │   │   └── AuthServiceImpl.java
│   │   │   ├── AuthService.java
│   │   │   └── TokenService.java
│   │   ├── utils/
│   │   │   └── PasswordUtils.java
│   │   └── SpringOauth2Application.java
│   └── resources/
│       └── application.properties
├── .gitignore
├── env.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
├── run.bat
└── run.sh
```

#### 2. Angular Frontend Project
```bash
angular_oauth2
├── .angular
├── node_modules
├── public
├── src/main/
│   ├── app/
│   │   ├── config/
│   │   ├── core/
│   │   │   ├── directives/
│   │   │   ├── pipes/
│   │   │   └── utils/
│   │   ├── main/
│   │   │   ├── components/
│   │   │   │   ├── footer/
│   │   │   │   ├── header/
│   │   │   │   ├── main/
│   │   │   │   └── sparta-ui/
│   │   │   └── guards/
│   │   │       ├── intercepts/
│   │   │       └── auth-guard.service.ts
│   │   ├── models/
│   │   ├── pages/
│   │   │   ├── home/
│   │   │   └── login/
│   │   ├── services/
│   │   │   └── auth/
│   │   ├── app.config.ts
│   │   └── app.routes.ts
│   ├── assets/
│   │   ├── icons/
│   │   └── img/
│   ├── environment/
│   │   └── environment.ts
│   ├── index.html
│   ├── main.ts
│   └── styles.css
├── .editorconfig
├── .gitignore
├── angular.json
├── package-lock.json
├── package.json
├── README.md
├── tailwind.config.js
├── tsconfig.app.json
├── tsconfig.json
└── tsconfig.spec.json
```

#### 3. Application Properties
Don't forget to add this to re-update the SQL DDL queries.
```java
spring.jpa.hibernate.ddl-auto=update
```

To enable Hibernate SQL logging, add:
```java
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

For Google OAuth 2.0 configuration, set up:
```java
# Google OAuth2 Client Configuration
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.redirect-uri=${REDIRECT_URI}
spring.security.oauth2.client.registration.google.scope=openid,profile,email
spring.security.oauth2.client.registration.google.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.google.user-info-uri=https://www.googleapis.com/oauth2/v3/userinfo

# Google OAuth2 Provider Configuration
spring.security.oauth2.client.provider.google.authorization-uri=https://accounts.google.com/o/oauth2/auth
spring.security.oauth2.client.provider.google.token-uri=https://oauth2.googleapis.com/token
spring.security.oauth2.client.provider.google.user-info-uri=https://www.googleapis.com/oauth2/v3/userinfo
spring.security.oauth2.client.provider.google.user-name-attribute=sub
```

### ⚙️ How to Run the Program

#### 1. The Backend Side
1. Navigate to the `spring_oauth2` directory:
    ```bash
    $ cd spring_oauth2
    ```
2. Ensure Maven is installed and check the version with:
    ```bash
    $ mvn -v
    ```
3. Set up your credentials by creating a file named `env.properties` in the root of the project, and fill it with:
    ```java
    DB_DATABASE=<your database url>
    DB_USER=<your database username>
    DB_PASSWORD=<your database password>
    PORT=<your preferred port number>
    JWT_SECRET_KEY=<jwt secret key>
    GOOGLE_CLIENT_ID=<your google client ID>
    GOOGLE_CLIENT_SECRET=<your google client secret>
    REDIRECT_URI=<preferable redirect URI>
    ```
4. For Windows, run the backend with:
    ```bash
    $ ./run.bat
    ```
   For Linux, make the script executable and run it with:
    ```bash
    $ chmod +x run.sh
    $ ./run.sh
    ```

   The backend will be accessible at [localhost:8080](http://localhost:8080).

#### 2. The Frontend Side
1. Navigate to the `angular_oauth2` directory:
    ```bash
    $ cd angular_oauth2
    ```
2. Install dependencies:
    ```bash
    $ npm install
    ```
3. Run the frontend with:
    ```bash
    $ npm run start
    ```

   The frontend will be accessible at [localhost:3000](http://localhost:3000).

### 🚀 Demonstration
The project features two types of authentication:
1. **Manual Login**: Users can input their username and password to log in.
2. **OAuth2 Login**: Allows users to log in using their Google account.

#### **Authentication Flow**

- **Frontend Flow:**
   1. **Manual Login:**
      - User inputs username/password.
      - Call the `/api/v1/authentication` endpoint to get the JWT token and store it in localStorage.
   2. **Google OAuth2 Login:**
      - User clicks the "Login with Google" button, which redirects them to the Google login page.
      - After successful Google login, they are redirected to the app with an authorization code.
      - Angular receives the authorization code from the URL and sends it to the backend (`/oauth2/callback`).
      - Backend exchanges the code for an OAuth2 token and generates a JWT for further usage.
   
- **Backend Flow:**
   1. **Manual Login:**
      - On username/password submission, validate credentials, generate a JWT, and return it to the client.
   2. **OAuth2 Login:**
      - On OAuth2 callback, exchange the authorization code for an OAuth2 token.
      - Fetch the user’s profile info from Google.
      - Generate a JWT based on the Google profile and return it to the client.

#### Screenshots

### Main Login Page
The main view of the login page featuring two authentication modes: manual and OAuth.

![Main Login Page](/Week%2013/Assignment%2001/img/main.png)

### Manual Authentication
Users can manually log in by entering their username and password into the form.

![Manual Authentication](/Week%2013/Assignment%2001/img/manual.png)

### OAuth Authentication
For OAuth login, users can click the "Sign in with Google" button to authenticate.

![OAuth Sign-In](/Week%2013/Assignment%2001/img/oauth1.png)

![OAuth Sign-In Continued](/Week%2013/Assignment%2001/img/oauth2.png)

### Authentication Success
Upon successful authentication, users are redirected to the home page.

![Authentication Success](/Week%2013/Assignment%2001/img/success.png)