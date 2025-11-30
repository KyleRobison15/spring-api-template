# Spring API Template

This template is an opinionated, production-ready REST API starter designed for applications where user authentication, authorization and management is a central requirement.

By using a convention-over-configuration approach to standardize the foundational patterns (repository-service-controller layers, MapStruct for DTOs, Flyway for migrations, centralized security configuration), developers can focus on implementing business logic rather than reinventing authentication flows and user management. The template's extensive documentation, TODO-driven customization checklist, and easily extensible architecture make it a valuable tool for solo devs and full on dev teams alike who want to focus on building amazing apps quickly, consistently and with best practices built in from the start.

Use this template to start building features in minutes instead of hours.

## 📚 What's Included

This template uses the [KRD Spring API Starter](../krd-spring-starters) which provides:

- **JWT Authentication** - Dual-token system (access + refresh)
- **User Management** - Complete CRUD with soft delete, re-activation, and hard delete after x days
- **Password Validation** - Configurable password policies (defaults to OWASP and NIST SP 800-63B password guidelines)
- **Role Management** - Add/remove roles with audit logging
- **Database Migrations** - V1 migration for users and roles tables (based on [starter template](https://github.com/KyleRobison15/krd-spring-starters/blob/main/spring-api-starter/src/main/resources/db/migration-templates/create_users_and_roles_tables.sql))
- **API Documentation** - Swagger UI with Springdoc OpenAPI
- **Security** - Pre-configured Spring Security with JWT filter

See [docs/STARTER_REFERENCE.md](docs/STARTER_REFERENCE.md) for complete documentation.

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

## ⚡ Quick Start

### Prerequisites
- Java 21+
- MySQL 8.0+
- Gradle 8.11+ (or use included wrapper)

### Setup

1. **Clone or use this template**
   ```bash
   # If using GitHub template: Click "Use this template" button
   # Or clone directly:
   git clone <your-repo-url>
   cd spring-api-template
   ```

2. **Configure environment variables**
   ```bash
   cp .env.example .env
   # Edit .env and update JWT_SECRET and DB_PASSWORD
   ```

3. **Create database**
   ```bash
   mysql -u root -p
   CREATE DATABASE spring_api_db;
   exit
   ```

4. **Run database migrations** (optional - migrations also run automatically on app startup)
   ```bash
   ./gradlew flywayMigrate
   ```

5. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

6. **Access Swagger UI**

   Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**That's it!** Your API is running with:
- ✅ JWT authentication (`/auth/login`, `/auth/refresh`, `/auth/me`)
- ✅ User management (`/users` endpoints)
- ✅ Password validation
- ✅ Role-based access control
- ✅ Soft delete with auto-reactivation
- ✅ API documentation

## 📋 Customization Checklist

Search for `TODO` comments throughout the codebase for all customization points. Here's a complete checklist:

### 1. Project Identity

- [ ] **`settings.gradle`** - Replace `spring-api-template` with your project name
  - *Why:* This is your Gradle project name used in builds

- [ ] **`build.gradle`** - Replace `com.krd` with your organization and `spring-api-template` with your project name
  - *Why:* Sets the Maven group ID for your artifacts

- [ ] **`src/main/java/com/krd/api/SpringApiTemplateApplication.java`** - Rename file and class to match your project
  - *Why:* Main application class should reflect your project name

### 2. Package Renaming

- [ ] **All Java files** - Replace package `com.krd.api` with `com.yourcompany.yourapp`
  - *Why:* Package names should reflect your organization
  - *Files affected:* All `.java` files in `src/main/java/com/krd/api/`

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

### 6. User Management

- [ ] **`application.yaml`** - Review `user-management.hard-delete.retention-days`
  - *Why:* Set data retention policy for your compliance requirements
  - *Default:* 365 days

- [ ] **`User.java`** - Add custom fields for your application
  - *Why:* Extend base user with domain-specific fields
  - *Example:* phoneNumber, birthDate, profilePictureUrl

- [ ] **`UserDto.java`** - Add custom fields to match User entity
  - *Why:* DTOs should include your custom fields for API responses

### 7. Environment Variables

- [ ] **`.env`** - Generate secure JWT_SECRET
  - *Why:* Default value is insecure
  - *Generate with:* `openssl rand -base64 32`

- [ ] **`.env`** - Set DB_PASSWORD for your local database
  - *Why:* Template password won't match your MySQL setup

- [ ] **`.env`** - Set production DATABASE_URL (when deploying)
  - *Why:* Production needs different database connection

- [ ] **`.env`** - Set production FRONTEND_URL (when deploying)
  - *Why:* Production CORS needs your actual frontend domain

### 8. Application Name

- [ ] **`application.yaml`** - Update `spring.application.name`
  - *Why:* Used in logging, monitoring, and service discovery

## 📖 Documentation

- [Getting Started Guide](docs/GETTING_STARTED.md) - Detailed setup and customization
- [Starter Reference](docs/STARTER_REFERENCE.md) - Complete API reference for the starter
- [Claude Code Guide](docs/CLAUDE.md) - Using this template with Claude Code

## 📄 License

MIT License - see [LICENSE](LICENSE) file for details.

## 👤 Author

**Kyle Robison**
- GitHub: [@KyleRobison15](https://github.com/KyleRobison15)
