// TODO: Rename package to match your organization (e.g., com.yourcompany.yourapp.auth)
package com.krd.api.auth;

import com.krd.api.users.User;
import com.krd.api.users.UserDto;
import com.krd.api.users.UserMapper;
import com.krd.starter.jwt.BaseAuthController;
import com.krd.starter.jwt.JwtConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller extending BaseAuthController from the spring-api-starter.
 *
 * For complete documentation of what BaseAuthController provides, see:
 * docs/STARTER_REFERENCE.md#jwt-authentication
 *
 * Inherited endpoints:
 * - POST /auth/login                  - Authenticate user and return JWT tokens
 * - POST /auth/refresh                - Refresh access token using refresh token
 * - GET  /auth/me                     - Get current authenticated user
 * - POST /auth/revoke-refresh-token   - Revoke refresh token (logout)
 *
 * Add custom authentication endpoints here if needed.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController extends BaseAuthController<User, UserDto> {

    public AuthController(JwtConfig jwtConfig,
                         UserMapper userMapper,
                         AuthService authService) {
        super(jwtConfig, userMapper, authService);
    }

    // All authentication endpoints are inherited from BaseAuthController

    // TODO: Add custom authentication endpoints here if needed
    // Example:
    // @PostMapping("/forgot-password")
    // public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) { ... }

}
