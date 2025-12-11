# Spring API Template

This template is an opinionated, production-ready REST API starter designed for applications where user authentication, authorization and management is a central requirement.

By using a convention-over-configuration approach to standardize the foundational patterns (repository-service-controller layers, MapStruct for DTOs, Flyway for migrations, centralized security configuration), developers can focus on implementing business logic rather than reinventing authentication flows and user management. The template's extensive documentation, TODO-driven customization checklist, and easily extensible architecture make it a valuable tool for solo devs and full on dev teams alike who want to focus on building amazing apps quickly, consistently and with best practices built in from the start.

Use this template to start building features in minutes instead of hours.

---

## 📑 Table of Contents

1. [About the Starter](#-about-the-starter)
2. [Technology Stack](#-technology-stack)
3. [Quick Start](#-quick-start)
4. [Customization Checklist](#-customization-checklist)
5. [Adding Custom Features](#-adding-custom-features)
6. [Testing Your API](#-testing-your-api)
7. [Deployment](#-deployment)
8. [Documentation & Resources](#-documentation--resources)

---

## 📚 About the Starter

This template uses the **[KRD Spring API Starter](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter)** (`com.krd:spring-api-starter:1.0.0`), which provides complete authentication and user management functionality out of the box.

### What the Starter Provides

- **JWT Authentication** - Dual-token system (access + refresh)
- **User Management** - Complete CRUD with soft delete & auto-reactivation
- **Password Validation** - Configurable password policies (OWASP/NIST compliant)
- **Role Management** - Role-based access control with audit logging
- **Automatic Exception Handling** - Auto-configured handlers for common errors (validation, auth, etc.)
- **Security** - Pre-configured Spring Security with modular SecurityRules
- **Scheduled Tasks** - Automatic cleanup of soft-deleted users

### Documentation Reference

For complete documentation about the starter's features, configuration, and API reference:

**👉 [Spring API Starter README](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter)** (Single source of truth)

**Quick Links to Key Sections:**

| Topic | Link |
|-------|------|
| **Getting Started** | [Quick Start](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#quick-start) · [Installation](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#installation) · [Configuration](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#8-configure-application) |
| **Core Components** | [BaseUser Entity](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#1-extend-baseuser) · [BaseUserRepository](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#2-create-repository) · [BaseUserService](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#5-create-service) |
| **Key Features** | [JWT Authentication](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#features) · [Exception Handling](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#exception-handling-architecture) · [Password Validation](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#features) |
| **API Reference** | [All Endpoints](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#api-endpoints) · [Exceptions](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#exceptions) · [Database Schema](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#database-schema) |

### How This Template Uses the Starter

- **User Entity** (`src/main/java/com/krd/api/users/User.java`) - Extends `BaseUser`, add custom fields here
- **UserService** (`src/main/java/com/krd/api/users/UserService.java`) - Extends `BaseUserService`, inherits all user management
- **Controllers** - Extend base controllers, automatically provide auth & user endpoints
- **ApiExceptionHandler** - Domain-specific exception handler (common exceptions handled automatically)
- **Configuration** - All starter features configured via YAML properties

### When to Reference What

- **Working on Authentication/Users?** → Reference the [Starter README](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter)
- **Customizing the Template?** → Use this README's [Customization Checklist](#-customization-checklist)
- **Adding New Features?** → See [Adding Custom Features](#-adding-custom-features) below
- **Using Claude Code?** → See [CLAUDE.md](CLAUDE.md) for AI-assisted workflows

---

## 🏗️ Technology Stack

- **Spring Boot 3.4.5** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Database access
- **MySQL** - Database
- **Flyway** - Database migrations
- **JWT (JJWT)** - Token-based authentication
- **MapStruct** - DTO mapping
- **Lombok** - Boilerplate reduction
- **Springdoc OpenAPI** - API documentation
- **Gradle** - Build tool

---

## ⚡ Quick Start

### Prerequisites

- **Java 21+** - [Download](https://adoptium.net/)
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)
- **Gradle 8.11+** - Optional, wrapper included

### Setup

**1. Clone or use this template**
```bash
# Click "Use this template" on GitHub
# Or clone directly:
git clone <your-repo-url>
cd spring-api-template
```

**2. Configure environment variables**
```bash
cp .env.example .env

# Generate a secure JWT secret
openssl rand -base64 32

# Edit .env and update:
# - JWT_SECRET (paste the generated secret)
# - DB_PASSWORD (your MySQL password)
```

**3. Create database**
```bash
mysql -u root -p
CREATE DATABASE spring_api_db;
exit
```

**4. Run the application**
```bash
./gradlew bootRun
```

**5. Access Swagger UI**

Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### ✅ You're Running!

Your API now has:
- ✅ JWT authentication (`/auth/login`, `/auth/refresh`, `/auth/me`)
- ✅ User management (`/users` endpoints)
- ✅ Password validation
- ✅ Role-based access control
- ✅ Soft delete with auto-reactivation
- ✅ Interactive API documentation

---

## 📋 Customization Checklist

Search for `TODO` comments throughout the codebase for all customization points. Here's a complete checklist:

### 1. Project Identity

- [ ] **`settings.gradle`** - Replace `spring-api-template` with your project name
  - *Why:* This is your Gradle project name used in builds

- [ ] **`build.gradle`** - Replace `com.krd` with your organization and `spring-api-template` with your project name
  - *Why:* Sets the Maven group ID for your artifacts

- [ ] **`src/main/java/com/krd/api/SpringApiTemplateApplication.java`** - Rename file and class to match your project
  - *Why:* Main application class should reflect your project name
  - *Also update:* The package names in `@EnableJpaRepositories` and `@EntityScan` annotations

### 2. Package Renaming

- [ ] **All Java files** - Replace package `com.krd.api` with `com.yourcompany.yourapp`
  - *Why:* Package names should reflect your organization
  - *Files affected:* All `.java` files in `src/main/java/com/krd/api/`
  - *Important:* Also update `@EnableJpaRepositories` and `@EntityScan` in main application class

### 3. Security Configuration

- [ ] **`application.yaml`** - Update `cors.allowed-origins` with your frontend URLs
  - *Why:* Controls which domains can access your API
  - *Default:* `http://localhost:5173`, `http://localhost:3000`

- [ ] **`application.yaml`** - Review JWT token expiration times
  - *Why:* Balance security vs. user experience for your use case
  - *Defaults:* 15 min access token, 7 day refresh token

- [ ] **`application.yaml`** - Review password policy requirements
  - *Why:* Adjust security requirements for your application
  - *Defaults:* 8-128 chars, requires uppercase, lowercase, digit, special char

- [ ] **`SecurityConfig.java`** - Review and add public endpoints
  - *Why:* Some endpoints may need to be accessible without authentication
  - *Example:* Public product listings, health checks, webhooks

### 4. Database Configuration

- [ ] **`application-dev.yaml`** - Update database name from `spring_api_db`
  - *Why:* Database name should match your project

- [ ] **`application-dev.yaml`** - Update database credentials
  - *Why:* Use your local MySQL username/password

- [ ] **`application-prod.yaml`** - Configure production database
  - *Why:* Production database will be different from local

- [ ] **`application-prod.yaml`** - Update CORS for production frontend
  - *Why:* Production frontend will have different domain

### 5. Logging Configuration

- [ ] **`application-dev.yaml`** - Update logging package from `com.krd.api`
  - *Why:* Logging should target your application's package

- [ ] **`application-prod.yaml`** - Update logging package from `com.krd.api`
  - *Why:* Logging should target your application's package

### 6. Exception Handling

- [ ] **`ApiExceptionHandler.java`** - Review domain-specific exception handlers
  - *Why:* Template shows how to handle domain exceptions (UserNotFoundException, DuplicateUserException)
  - *Location:* `src/main/java/com/krd/api/common/ApiExceptionHandler.java`
  - *Note:* Common exceptions (validation, auth, etc.) are handled automatically by exception-handling-starter

- [ ] **`ApiExceptionHandler.java`** - Add handlers for your custom domain exceptions
  - *Why:* As you add new features (e.g., Products), add exception handlers for domain-specific errors
  - *Example:* Add `handleProductNotFoundException` for a Products feature

### 7. User Management

- [ ] **`application.yaml`** - Review `app.user.hard-delete-after-days`
  - *Why:* Set data retention policy for your compliance requirements
  - *Default:* 365 days

- [ ] **`User.java`** - Add custom fields for your application
  - *Why:* Extend base user with domain-specific fields
  - *Example:* phoneNumber, birthDate, profilePictureUrl

- [ ] **`UserDto.java`** - Add custom fields to match User entity
  - *Why:* DTOs should include your custom fields for API responses

### 8. Environment Variables

- [ ] **`.env`** - Generate secure JWT_SECRET
  - *Why:* Default value is insecure
  - *Generate with:* `openssl rand -base64 32`

- [ ] **`.env`** - Set DB_PASSWORD for your local database
  - *Why:* Template password won't match your MySQL setup

- [ ] **`.env`** - Set production DATABASE_URL (when deploying)
  - *Why:* Production needs different database connection

- [ ] **`.env`** - Set production FRONTEND_URL (when deploying)
  - *Why:* Production CORS needs your actual frontend domain

### 9. Application Name

- [ ] **`application.yaml`** - Update `spring.application.name`
  - *Why:* Used in logging, monitoring, and service discovery

---

## 🔧 Adding Custom Features

### Database Migrations

This template includes a V1 migration that creates the base tables required by the spring-api-starter. See the [Database Schema](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#database-schema) documentation for details.

**Start your custom migrations from V2__:**
- `V2__add_custom_user_fields.sql`
- `V3__create_orders_table.sql`
- `V4__create_products_table.sql`

**Running migrations:**
```bash
./gradlew flywayMigrate  # Run all pending migrations
./gradlew flywayInfo     # Show migration status
```

Migrations also run automatically on application startup.

### Adding Custom User Fields

The template's `User` entity extends `BaseUser` from the spring-api-starter. See the [BaseUser Entity](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#1-extend-baseuser) documentation for all inherited fields.

**1. Update the User entity:**

`src/main/java/com/yourcompany/yourapp/users/User.java`:
```java
@Entity
@Table(name = "users")
public class User extends BaseUser {
    // Add your custom fields
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;
}
```

**2. Update the UserDto:**

`src/main/java/com/yourcompany/yourapp/users/UserDto.java`:
```java
public class UserDto extends BaseUserDto {
    // Add matching fields
    private String phoneNumber;
    private LocalDate birthDate;
}
```

**3. Create a Flyway migration:**

`src/main/resources/db/migration/V2__add_custom_user_fields.sql`:
```sql
ALTER TABLE users
ADD COLUMN phone_number VARCHAR(20),
ADD COLUMN birth_date DATE;
```

**4. MapStruct automatically maps matching fields** - no mapper changes needed!

### Adding New Endpoints

**1. Create an entity:**

`src/main/java/com/yourcompany/yourapp/products/Product.java`:
```java
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;
}
```

**2. Create a repository:**

`src/main/java/com/yourcompany/yourapp/products/ProductRepository.java`:
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String keyword);
}
```

**3. Create a service:**

`src/main/java/com/yourcompany/yourapp/products/ProductService.java`:
```java
@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}
```

**4. Create a controller:**

`src/main/java/com/yourcompany/yourapp/products/ProductController.java`:
```java
@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Tag(name = "Products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }
}
```

**5. Create a Flyway migration:**

`src/main/resources/db/migration/V3__create_products_table.sql`:
```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Configuring Security Rules

The template uses the **SecurityRules pattern** from the spring-api-starter for modular security configuration.

**Default behavior:** All endpoints require authentication except:
- `/auth/**` (login, refresh)
- `POST /users` (registration)
- `/swagger-ui/**`, `/v3/api-docs/**` (API docs)

**To add custom security rules:**

```java
@Component
public class ProductSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>
                         .AuthorizationManagerRequestMatcherRegistry registry) {
        registry
            .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/products").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN");
    }
}
```

The starter automatically discovers and applies all `SecurityRules` components. See the template's existing examples:
- `src/main/java/com/krd/api/users/UserSecurityRules.java`
- `src/main/java/com/krd/api/auth/AuthSecurityRules.java`

For complete security documentation, see the [spring-api-starter Security documentation](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#features).

### Exception Handling

This template uses a **two-tier exception handling architecture**:

#### 1. Automatic Exception Handling (from exception-handling-starter)

Common exceptions are handled automatically with zero configuration:
- Validation errors (400)
- Malformed JSON (400)
- Authentication failures (401)
- Authorization denials (403)
- Unsupported media types (415)
- Unexpected errors (500)

**No code needed!** These are handled by the `GlobalExceptionHandler` from `exception-handling-starter`.

#### 2. Domain-Specific Exception Handling (your application)

The template includes `ApiExceptionHandler` showing how to handle **domain-specific exceptions**:
- `UserNotFoundException` (404)
- `DuplicateUserException` (409)

**To add custom exception handlers:**

Add a new handler method to `src/main/java/com/krd/api/common/ApiExceptionHandler.java`:

```java
@ExceptionHandler(ProductNotFoundException.class)
public ResponseEntity<ErrorResponse> handleProductNotFoundException(
        ProductNotFoundException ex, WebRequest request) {

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getDescription(false).replace("uri=", ""))
        .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
}
```

**Note:** Import `ErrorResponse` from `com.krd.starter.exception.ErrorResponse`

For complete documentation on the two-tier exception handling architecture, error response format, and best practices, see the [Exception Handling Architecture](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#exception-handling-architecture) section in the starter README.

---

## 🧪 Testing Your API

### Using Swagger UI

1. Open http://localhost:8080/swagger-ui.html
2. Find **POST /users** (Register User)
3. Click "Try it out"
4. Use this request body:

```json
{
  "email": "admin@example.com",
  "password": "Admin123!",
  "firstName": "Admin",
  "lastName": "User",
  "username": "admin"
}
```

5. Click "Execute" - you should get a 200 response

### Testing Authentication

**1. Register a user** (as shown above)

**2. Login:**

Find **POST /auth/login**:
```json
{
  "email": "admin@example.com",
  "password": "Admin123!"
}
```

Response:
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc..."
}
```

**3. Use the access token:**

Click "Authorize" button at the top of Swagger UI:
- Enter: `Bearer eyJhbGc...` (your access token)
- Click "Authorize"

Now you can access protected endpoints!

### Testing with cURL

**Register:**
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "User123!",
    "firstName": "Test",
    "lastName": "User"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "User123!"
  }'
```

**Get current user:**
```bash
curl http://localhost:8080/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## 🚀 Deployment

### Environment Variables

Set these in your production environment:

```bash
JWT_SECRET=<generated-secure-secret>
DB_USERNAME=<production-db-user>
DB_PASSWORD=<production-db-password>
DATABASE_URL=jdbc:mysql://<host>:3306/<database>
FRONTEND_URL=https://your-frontend-domain.com
SPRING_PROFILES_ACTIVE=prod
```

### Database Migrations

Flyway will automatically run migrations on startup. Ensure:
- `V1__` migration creates users/roles tables (from starter)
- `V2__+` migrations add your custom tables/fields

### Build for Production

```bash
# Create executable JAR
./gradlew clean build

# JAR will be in: build/libs/your-app-name-0.0.1-SNAPSHOT.jar
```

### Run in Production

```bash
java -jar build/libs/your-app-name-0.0.1-SNAPSHOT.jar
```

Or deploy to Railway, AWS Elastic Beanstalk, Docker, etc.

---

## 📖 Documentation & Resources

### Template Documentation

- **[Claude Code Guide](CLAUDE.md)** - AI-assisted development with this template
- **This README** - Complete template setup and customization guide

### Starter Documentation

- **[Spring API Starter README](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter)** - Complete feature documentation
  - [Quick Start Guide](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#quick-start)
  - [Configuration Options](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#8-configure-application)
  - [API Endpoints](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#api-endpoints)
  - [Exception Handling Architecture](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#exception-handling-architecture)
  - [Database Schema](https://github.com/KyleRobison15/krd-spring-starters/tree/main/spring-api-starter#database-schema)

### Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/index.html)
- [MapStruct Documentation](https://mapstruct.org/)
- [Flyway Documentation](https://flywaydb.org/documentation/)

---

## 📄 License

MIT License - see [LICENSE](LICENSE) file for details.

## 👤 Author

**Kyle Robison**
- GitHub: [@KyleRobison15](https://github.com/KyleRobison15)
