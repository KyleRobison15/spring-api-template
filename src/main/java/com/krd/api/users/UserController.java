// TODO: Rename package to match your organization (e.g., com.yourcompany.yourapp.users)
package com.krd.api.users;

import com.krd.starter.user.BaseUserController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller extending BaseUserController from the spring-api-starter.
 *
 * For complete documentation of what BaseUserController provides, see:
 * docs/STARTER_REFERENCE.md#baseuserservice
 *
 * Inherited endpoints:
 * - GET    /users              - List all users (sorted by email, firstName, lastName, or username)
 * - GET    /users/{id}         - Get user by ID
 * - POST   /users              - Register new user (with password validation)
 * - PUT    /users/{id}         - Update user (authorization: self or ADMIN)
 * - DELETE /users/{id}         - Soft delete user (ADMIN only)
 * - POST   /users/{id}/change-password - Change password (authorization: self only)
 * - POST   /users/{id}/roles   - Add role to user (ADMIN only)
 * - DELETE /users/{id}/roles   - Remove role from user (ADMIN only)
 *
 * All endpoints include proper authorization, validation, and error handling.
 *
 * Add custom user endpoints here if needed.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserController extends BaseUserController<User, UserDto> {

    public UserController(UserService service) {
        super(service);
    }

    // All user management endpoints are inherited from BaseUserController

    // TODO: Add custom user endpoints here if needed
    // Example:
    // @GetMapping("/{id}/orders")
    // public List<Order> getUserOrders(@PathVariable Long id) { ... }

}
