// TODO: Rename package to match your organization (e.g., com.yourcompany.yourapp.users)
package com.krd.api.users;

import com.krd.starter.user.BaseUserService;
import com.krd.starter.user.RoleChangeLogRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * User service extending BaseUserService from the spring-api-starter.
 *
 * For complete documentation of what BaseUserService provides, see:
 * docs/STARTER_REFERENCE.md#baseuserservice
 *
 * Add custom business logic methods here if needed.
 */
@Service
public class UserService extends BaseUserService<User, UserDto> {

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            RoleChangeLogRepository roleChangeLogRepository) {
        super(userRepository, userMapper, passwordEncoder, roleChangeLogRepository);
    }

    // TODO: Add custom business logic methods here

}
