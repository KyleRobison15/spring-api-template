// TODO: Rename package to match your organization (e.g., com.yourcompany.yourapp.auth)
package com.krd.api.auth;

import com.krd.api.users.User;
import com.krd.api.users.UserRepository;
import com.krd.starter.jwt.BaseAuthService;
import com.krd.starter.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

/**
 * Authentication service extending BaseAuthService from the spring-api-starter.
 *
 * For complete documentation of what BaseAuthService provides, see:
 * docs/STARTER_REFERENCE.md#jwt-authentication
 *
 * Provides:
 * - login(LoginRequest): LoginResponse - Authenticate user and return tokens
 * - refresh(String refreshToken): JwtResponse - Refresh access token
 * - getCurrentUser(): User - Get currently authenticated user
 *
 * Add custom authentication methods here if needed.
 */
@Service
public class AuthService extends BaseAuthService<User> {

    public AuthService(AuthenticationManager authenticationManager,
                      UserRepository userRepository,
                      JwtService jwtService) {
        super(authenticationManager, userRepository, jwtService);
    }

    // TODO: Add custom authentication methods here if needed
    // Example:
    // public void sendPasswordResetEmail(String email) { ... }
    // public void verifyEmail(String token) { ... }

}
