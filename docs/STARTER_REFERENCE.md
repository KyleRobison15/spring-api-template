# Spring API Starter - Reference Guide

This document provides a complete reference for what the `com.krd:spring-api-starter` provides out of the box.

## 📦 What's Included

The spring-api-starter is a "batteries included" starter that provides complete authentication and user management functionality with a single dependency.

---

## 👤 BaseUser Entity

**Package:** `com.krd.starter.user.BaseUser`

A `@MappedSuperclass` that your User entity should extend.

### Fields Provided:

| Field | Type | Database Column | Constraints | Description |
|-------|------|----------------|-------------|-------------|
| `id` | `Long` | `id` | Primary key, auto-generated | User ID |
| `firstName` | `String` | `first_name` | - | User's first name |
| `lastName` | `String` | `last_name` | - | User's last name |
| `username` | `String` | `username` | Unique | Display name (e.g., @username) |
| `email` | `String` | `email` | Unique, not null | Login identifier |
| `password` | `String` | `password` | Not null | BCrypt hashed password |
| `roles` | `Set<String>` | `user_roles` table | Eager fetch | Authorization roles (e.g., "USER", "ADMIN") |
| `enabled` | `boolean` | `enabled` | Not null, default true | Account status |
| `deletedAt` | `LocalDateTime` | `deleted_at` | - | Soft delete timestamp |

### Important Notes:

- **Use `@SuperBuilder`** instead of `@Builder` when extending BaseUser
- **Don't duplicate constructor annotations** - `@NoArgsConstructor` and `@AllArgsConstructor` are inherited from BaseUser
- **Implements `JwtUser`** interface for JWT authentication
- **Roles are stored** in a separate `user_roles` table with CASCADE delete
- **Soft delete logic**: When `deletedAt` is not null, user is considered deleted
- **Email modification**: On soft delete, "_deleted" is appended to free unique constraint
- **Username preservation**: Username remains unchanged on soft delete to preserve identity

### Usage Example:

```java
@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder  // Important: Use SuperBuilder, not Builder
public class User extends BaseUser {
    // Add your custom fields here
    @Column(name = "phone_number")
    private String phoneNumber;
}
```

**Note:** Don't add `@NoArgsConstructor` or `@AllArgsConstructor` to your User class - these are inherited from BaseUser and will cause compilation errors if duplicated.

---

## 📄 BaseUserDto

**Package:** `com.krd.starter.user.dto.BaseUserDto`

Abstract DTO for user data transfer. Password and deletedAt are excluded for security.

### Fields Provided:

| Field | Type | Description |
|-------|------|-------------|
| `id` | `Long` | User ID |
| `firstName` | `String` | User's first name |
| `lastName` | `String` | User's last name |
| `username` | `String` | Display name |
| `email` | `String` | Email address |
| `roles` | `Set<String>` | User roles |
| `enabled` | `boolean` | Account status |

### Important Notes:

- **Use `@SuperBuilder`** when extending BaseUserDto
- **Don't duplicate constructor annotations** - these are inherited from BaseUserDto
- **Password is excluded** for security
- **deletedAt is excluded** (internal field)

---

## 🗄️ BaseUserRepository

**Package:** `com.krd.starter.user.BaseUserRepository<T>`

Generic repository interface with built-in soft delete support.

### Methods Provided:

#### Standard Queries (exclude soft-deleted users):

- `findById(Long id): Optional<T>` - Find by ID
- `findAll(): List<T>` - Get all users
- `findAll(Sort sort): List<T>` - Get all users with sorting
- `findByEmail(String email): Optional<T>` - Find by email
- `existsByEmail(String email): boolean` - Check email exists
- `existsByUsername(String username): boolean` - Check username exists
- `countByRolesContaining(String role): long` - Count users with role

#### Special Queries (include soft-deleted):

- `findByEmailIncludingDeleted(String email): Optional<T>` - For auto-reactivation logic
- `existsByUsernameIncludingDeleted(String username): boolean` - For registration validation
- `findByDeletedAtBefore(LocalDateTime threshold): List<T>` - For hard delete scheduler

#### JpaRepository Methods:

All standard JpaRepository methods (save, delete, etc.) are also available.

### Usage Example:

```java
@Repository
public interface UserRepository extends BaseUserRepository<User> {
    // Add custom queries here if needed
}
```

---

## 🔧 BaseUserService

**Package:** `com.krd.starter.user.BaseUserService<T, D>`

Generic service providing complete user management functionality.

### Constructor Dependencies:

- `BaseUserRepository<T> repository`
- `BaseUserMapper<T, D> mapper`
- `PasswordEncoder passwordEncoder`
- `RoleChangeLogRepository roleChangeLogRepository`

### Methods Provided:

#### User Management:

- `getAllUsers(String sort): Iterable<D>` - Get all users with sorting (firstName, lastName, username, email)
- `getUser(Long id): D` - Find user by ID
- `registerUser(RegisterUserRequest): D` - Register new user with validation and auto-reactivation
- `updateUser(Long id, UpdateUserRequest): D` - Update user details
- `deleteUser(Long id): void` - Soft delete user
- `changePassword(Long id, ChangePasswordRequest): void` - Change password

#### Role Management:

- `addRole(Long id, AddRoleRequest): D` - Add role to user (logs to audit)
- `removeRole(Long id, RemoveRoleRequest): D` - Remove role from user (logs to audit)

### Features:

#### Auto-Reactivation:
If a user registers with an email that belongs to a soft-deleted account:
- The account is automatically reactivated
- deletedAt is set to null
- enabled is set to true
- Email suffix "_deleted" is removed
- Password is updated to the new one
- firstName and lastName are updated if provided
- Username is preserved from original account

#### Soft Delete:
- Sets deletedAt timestamp
- Disables the account (enabled = false)
- Appends "_deleted" to email to free unique constraint
- Username remains unchanged to preserve identity

#### Authorization:
- Users can update their own profile and change their own password
- Admins can update any profile
- Admins cannot delete themselves (prevents lockout)
- Cannot delete the last admin user
- Admins cannot remove their own ADMIN role

#### Validation:
- Email uniqueness (active accounts only)
- Username uniqueness (all accounts, including deleted)
- Password requirements (via PasswordValidator)
- Role validation (users must have at least one role)

### Usage Example:

```java
@Service
public class UserService extends BaseUserService<User, UserDto> {
    public UserService(
            UserRepository repository,
            UserMapper mapper,
            PasswordEncoder passwordEncoder,
            RoleChangeLogRepository roleChangeLogRepository) {
        super(repository, mapper, passwordEncoder, roleChangeLogRepository);
    }

    // Add custom methods here
}
```

---

## 🔄 BaseUserMapper

**Package:** `com.krd.starter.user.BaseUserMapper<T, D>`

MapStruct mapper interface for entity/DTO conversions.

### Methods Provided:

- `toDto(T user): D` - Convert entity to DTO
- `toEntity(RegisterUserRequest request): T` - Convert registration request to entity
- `update(UpdateUserRequest request, @MappingTarget T user): void` - Update existing entity

### Usage Example:

```java
@Mapper(componentModel = "spring")
public interface UserMapper extends BaseUserMapper<User, UserDto> {
    // MapStruct generates implementation automatically
    // Add custom mappings here if needed
}
```

---

## 🔐 JWT Authentication

### Provided Components:

- `JwtService` - Token generation and parsing
- `JwtAuthenticationFilter` - Spring Security filter
- `BaseAuthService<T>` - Generic auth service
- `BaseAuthController<T, D>` - Generic auth controller

### DTOs:

- `LoginRequest` - email + password
- `LoginResponse` - accessToken + refreshToken
- `JwtResponse` - accessToken only (for refresh)

### Configuration Properties:

```yaml
jwt:
  secret: your-secret-key-here  # Required
  access-token-expiration: 900000  # 15 minutes (milliseconds)
  refresh-token-expiration: 2592000000  # 30 days (milliseconds)
```

---

## ✅ Password Validation

### Configuration Properties:

```yaml
password-policy:
  min-length: 8
  max-length: 128
  require-uppercase: true
  require-lowercase: true
  require-digit: true
  require-special-char: true
```

### Validation Annotation:

```java
@ValidPassword
private String password;
```

---

## 🗃️ Database Migrations

### Provided Flyway Migration:

**File:** `V1__create_users_and_roles_tables.sql`

Creates:
- `users` table with all BaseUser fields
- `user_roles` table with CASCADE delete
- `role_change_logs` table with SET NULL delete (preserves audit trail)
- Appropriate indexes

### Custom Migrations:

Start your custom migrations from `V10__` to leave room for future starter updates:

```sql
-- V10__add_custom_user_fields.sql
ALTER TABLE users ADD COLUMN phone_number VARCHAR(20);
ALTER TABLE users ADD COLUMN birth_date DATE;
```

---

## 🔧 Auto-Configuration

The starter provides `SpringApiStarterAutoConfiguration` which automatically configures:

- `PasswordEncoder` (BCrypt)
- `JwtService`
- `JwtAuthenticationFilter`
- `AuthenticationManager`
- Spring Security with stateless JWT
- Method-level security (`@PreAuthorize`)
- `@EnableScheduling` for hard delete scheduler
- `UserManagementConfig` for hard delete settings

### Hard Delete Configuration:

```yaml
user-management:
  hard-delete:
    enabled: true  # Enable/disable scheduled hard delete
    retention-days: 30  # Days to keep soft-deleted users before hard delete
```

---

## 📚 Request/Response DTOs

### Registration:

```java
// RegisterUserRequest
{
  "email": "user@example.com",
  "password": "SecurePass123!",
  "firstName": "John",      // Optional
  "lastName": "Doe",        // Optional
  "username": "johndoe"     // Optional
}
```

### Update User:

```java
// UpdateUserRequest
{
  "email": "newemail@example.com",    // Optional
  "firstName": "Jane",                // Optional
  "lastName": "Smith",                // Optional
  "username": "janesmith"             // Optional
}
```

### Change Password:

```java
// ChangePasswordRequest
{
  "oldPassword": "OldPass123!",
  "newPassword": "NewPass123!",
  "confirmPassword": "NewPass123!"
}
```

### Add/Remove Role:

```java
// AddRoleRequest
{
  "role": "ADMIN"
}

// RemoveRoleRequest
{
  "role": "MANAGER"
}
```

---

## 📖 Additional Resources

- **Main Starter README:** `/Users/robison/MyDev/Spring_Starters/krd-spring-starters/spring-api-starter/README.md`
- **JWT Auth Starter README:** `/Users/robison/MyDev/Spring_Starters/krd-spring-starters/jwt-auth-starter/README.md`
- **Source Code:** `/Users/robison/MyDev/Spring_Starters/krd-spring-starters/`
