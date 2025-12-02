# Getting Started with Spring API Template

This guide walks you through setting up and customizing the Spring API Template for your project.

## 📋 Table of Contents

1. [Initial Setup](#initial-setup)
2. [Customizing the Template](#customizing-the-template)
3. [Adding Custom Features](#adding-custom-features)
4. [Testing Your API](#testing-your-api)
5. [Deploying to Production](#deploying-to-production)

---

## Initial Setup

### 1. Prerequisites

Ensure you have the following installed:

- **Java 21+**: [Download](https://adoptium.net/)
- **MySQL 8.0+**: [Download](https://dev.mysql.com/downloads/mysql/)
- **Gradle 8.11+**: (Optional, wrapper included)
- **Git**: For version control
- **IDE**: IntelliJ IDEA, VS Code, or Eclipse

### 2. Create Your Project

**Option A: Use as GitHub Template**
1. Click "Use this template" on GitHub
2. Name your repository
3. Clone your new repository

**Option B: Clone Directly**
```bash
git clone <repository-url>
cd spring-api-template
```

### 3. Set Up Environment Variables

```bash
# Copy the example file
cp .env.example .env

# Generate a secure JWT secret
openssl rand -base64 32

# Edit .env and update:
# - JWT_SECRET (paste the generated secret)
# - DB_PASSWORD (your MySQL root password)
```

### 4. Create Database

```bash
mysql -u root -p
```

```sql
CREATE DATABASE spring_api_db;
exit
```

### 5. Run the Application

```bash
./gradlew bootRun
```

The API will start on `http://localhost:8080`

### 6. Verify Setup

Open Swagger UI: http://localhost:8080/swagger-ui/index.html

You should see the API documentation with:
- Auth endpoints (`/auth/*`)
- User endpoints (`/users/*`)

---

## Customizing the Template

Follow these steps to make the template your own.

### Step 1: Rename the Project

**Update `settings.gradle`:**
```gradle
// Change from:
rootProject.name = 'spring-api-template'

// To:
rootProject.name = 'my-awesome-api'
```

**Update `build.gradle`:**
```gradle
// Change from:
group = 'com.krd'

// To:
group = 'com.mycompany'
```

**Update `application.yaml`:**
```yaml
# Change from:
spring:
  application:
    name: spring-api-template

# To:
spring:
  application:
    name: my-awesome-api
```

### Step 2: Rename Packages

Rename the base package from `com.krd.api` to your organization:

```
src/main/java/com/krd/api/
  ↓
src/main/java/com/mycompany/myapp/
```

**In IntelliJ IDEA:**
1. Right-click on `com.krd.api` package
2. Refactor → Rename
3. Enter your package name (e.g., `com.mycompany.myapp`)
4. Click "Refactor"

**Manual method:**
1. Create new package: `src/main/java/com/mycompany/myapp/`
2. Move all files to new package
3. Update imports throughout the project
4. Delete old `com/krd/api/` directory

**Update logging configuration:**

`application-dev.yaml`:
```yaml
logging:
  level:
    com.mycompany.myapp: DEBUG  # Update package name
```

`application-prod.yaml`:
```yaml
logging:
  level:
    com.mycompany.myapp: INFO  # Update package name
```

**Update JPA repository and entity scanning:**

After renaming your package, you must update the `@EnableJpaRepositories` and `@EntityScan` annotations in your main application class to scan both your new package and the starter package:

`SpringApiTemplateApplication.java`:
```java
@EnableJpaRepositories(basePackages = {"com.mycompany.myapp", "com.krd.starter.user"})
@EntityScan(basePackages = {"com.mycompany.myapp", "com.krd.starter.user"})
```

**Why is this needed?** The starter provides repositories and entities (like `RoleChangeLogRepository` and `RoleChangeLog`) that your application needs to access. These annotations tell Spring Data JPA to scan for repositories and entities in both your application package and the starter package.

### Step 3: Rename Main Application Class

Rename `SpringApiTemplateApplication.java` to match your project:

```java
// Before:
public class SpringApiTemplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringApiTemplateApplication.class, args);
    }
}

// After:
public class MyAwesomeApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyAwesomeApiApplication.class, args);
    }
}
```

### Step 4: Configure CORS

Update `application.yaml` with your frontend URLs:

```yaml
cors:
  allowed-origins:
    - http://localhost:5173  # Your dev frontend
    - https://myapp.com      # Your production frontend
```

### Step 5: Configure Database

**Development (`application-dev.yaml`):**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/my_app_db?createDatabaseIfNotExist=true
    username: root
    password: ${DB_PASSWORD}
```

**Production (`application-prod.yaml`):**
```yaml
spring:
  datasource:
    url: ${DATABASE_URL}  # Set via environment variable
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### Step 6: Review Security Settings

**JWT Token Expiration (`application.yaml`):**
```yaml
spring:
  jwt:
    accessTokenExpiration: 900      # 15 minutes
    refreshTokenExpiration: 604800  # 7 days
```

**Password Policy (`application.yaml`):**
```yaml
app:
  security:
    password:
      min-length: 8
      require-uppercase: true
      require-lowercase: true
      require-digit: true
      require-special-char: true
```

**Hard Delete Retention (`application.yaml`):**
```yaml
user-management:
  hard-delete:
    enabled: true
    retention-days: 365  # Adjust for your compliance requirements
```

---

## Adding Custom Features

### Database Migrations

This template includes a V1 migration (`src/main/resources/db/migration/V1__create_users_and_roles_tables.sql`) that creates the base tables for user authentication:
- `users` - User accounts with authentication fields
- `user_roles` - User role assignments
- `role_change_logs` - Audit trail for role changes

**Migration Template Reference:**

The V1 migration is based on the template maintained in the spring-api-starter repository. If the starter is updated with schema changes, you can reference the latest version here:

[Migration Template in spring-api-starter](https://github.com/KyleRobison15/krd-spring-starters/blob/main/spring-api-starter/src/main/resources/db/migration-templates/create_users_and_roles_tables.sql)

**Custom Migration Numbering:**

Start your custom migrations from `V2__`:
- `V2__add_custom_user_fields.sql`
- `V3__create_orders_table.sql`
- `V4__create_products_table.sql`
- etc.

**Running Migrations:**

Migrations run automatically when you start the application, but you can also run them manually using the Flyway Gradle plugin:

```bash
# Run all pending migrations
./gradlew flywayMigrate

# Show migration status
./gradlew flywayInfo

# Validate applied migrations
./gradlew flywayValidate

# Repair metadata table (if needed)
./gradlew flywayRepair

# Clean database (DANGER: drops all objects!)
./gradlew flywayClean
```

The Flyway plugin is configured in `build.gradle` to use the same database connection as your application (from `.env` file).

### Adding Custom User Fields

**1. Update the User entity:**

`src/main/java/com/mycompany/myapp/users/User.java`:
```java
@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseUser {

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
}
```

**2. Update the UserDto:**

`src/main/java/com/mycompany/myapp/users/UserDto.java`:
```java
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserDto extends BaseUserDto {

    private String phoneNumber;
    private LocalDate birthDate;
    private String profilePictureUrl;
}
```

**3. Create a Flyway migration:**

`src/main/resources/db/migration/V2__add_custom_user_fields.sql`:
```sql
ALTER TABLE users
ADD COLUMN phone_number VARCHAR(20),
ADD COLUMN birth_date DATE,
ADD COLUMN profile_picture_url VARCHAR(512);
```

**4. MapStruct will automatically map matching fields!**

No changes needed to `UserMapper` if field names match.

### Adding New Endpoints

**1. Create a new entity:**

`src/main/java/com/mycompany/myapp/products/Product.java`:
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

`src/main/java/com/mycompany/myapp/products/ProductRepository.java`:
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String keyword);
}
```

**3. Create a service:**

`src/main/java/com/mycompany/myapp/products/ProductService.java`:
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

`src/main/java/com/mycompany/myapp/products/ProductController.java`:
```java
@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Tag(name = "Products", description = "Product management endpoints")
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

**5. Create Flyway migration:**

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

The template uses a **modular security architecture** powered by the `spring-api-starter`. Security rules are automatically discovered and applied without requiring a custom `SecurityConfig`.

#### Default Public Endpoints

The starter automatically makes these endpoints public (no authentication required):
- `/auth/**` - Login, refresh token, logout
- `POST /users` - User registration
- `/swagger-ui/**`, `/v3/api-docs/**` - API documentation
- `GET /actuator/health` - Health check

**All other endpoints require authentication by default.**

#### Adding Custom Security Rules

To make your application-specific endpoints public or require specific roles, create a `SecurityRules` component:

**Example: Product Security Rules**

`src/main/java/com/mycompany/myapp/products/ProductSecurityRules.java`:
```java
package com.mycompany.myapp.products;

import com.krd.security.SecurityRules;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

/**
 * Security rules for product endpoints.
 */
@Component
public class ProductSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
            // Allow anyone to browse products (no authentication)
            .requestMatchers(HttpMethod.GET, "/products/**").permitAll()

            // Require authentication to create products
            .requestMatchers(HttpMethod.POST, "/products").authenticated()

            // Require ADMIN role to delete products
            .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN");
    }
}
```

**That's it!** The starter automatically:
1. Discovers all `@Component` classes implementing `SecurityRules`
2. Applies their rules to the security filter chain
3. No `SecurityConfig` needed

#### Multiple Security Rules

You can have as many `SecurityRules` components as needed - organize by feature:

```java
// OrderSecurityRules.java
@Component
public class OrderSecurityRules implements SecurityRules {
    @Override
    public void configure(...registry) {
        registry
            .requestMatchers("/orders/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/orders/public/**").permitAll();
    }
}

// AdminSecurityRules.java
@Component
public class AdminSecurityRules implements SecurityRules {
    @Override
    public void configure(...registry) {
        registry
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/analytics/**").hasAnyRole("ADMIN", "ANALYST");
    }
}
```

#### Advanced: Custom SecurityFilterChain

**You typically don't need this.** Only create a custom `SecurityFilterChain` if you need to:
- Add custom authentication filters
- Change session management
- Modify exception handling
- Customize the entire security setup

If you do need custom configuration:

`src/main/java/com/mycompany/myapp/config/CustomSecurityConfig.java`:
```java
@Configuration
public class CustomSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthFilter,
            CorsConfigurationSource corsConfigurationSource,
            List<SecurityRules> securityRules) throws Exception {

        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                // Apply all discovered SecurityRules
                securityRules.forEach(rule -> rule.configure(auth));

                // Your custom base rules
                auth.requestMatchers("/custom/**").permitAll()
                    .anyRequest().authenticated();
            })
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

**Note:** Creating this bean overrides the starter's default `SecurityFilterChain` because the starter uses `@ConditionalOnMissingBean`.

---

## Testing Your API

### Using Swagger UI

1. Open http://localhost:8080/swagger-ui.html
2. Find the `/users` → `POST /users` endpoint (Register User)
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

5. Click "Execute"
6. You should get a 200 response with the created user

### Testing Authentication

**1. Register a user** (as shown above)

**2. Login:**

Find `/auth` → `POST /auth/login`:
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

## Deploying to Production

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
- `V1__` migration (from starter) creates users/roles tables
- `V10__+` migrations add your custom tables/fields

### Build for Production

```bash
# Create executable JAR
./gradlew clean build

# JAR will be in: build/libs/my-awesome-api-0.0.1-SNAPSHOT.jar
```

### Run in Production

```bash
java -jar build/libs/my-awesome-api-0.0.1-SNAPSHOT.jar
```

Or use Docker, Railway, AWS Elastic Beanstalk, etc.

---

## Next Steps

- Review [STARTER_REFERENCE.md](STARTER_REFERENCE.md) for complete API documentation
- Add your business logic and custom endpoints
- Set up CI/CD pipeline
- Configure monitoring and logging
- Add integration tests

---

**Need Help?**

- Check existing TODO comments in the code
- Review Swagger UI for API documentation
- See [STARTER_REFERENCE.md](STARTER_REFERENCE.md) for starter details
