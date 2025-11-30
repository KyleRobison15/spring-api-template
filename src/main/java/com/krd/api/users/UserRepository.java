// TODO: Rename package to match your organization (e.g., com.yourcompany.yourapp.users)
package com.krd.api.users;

import com.krd.starter.user.BaseUserRepository;
import org.springframework.stereotype.Repository;

/**
 * User repository extending BaseUserRepository from the spring-api-starter.
 *
 * For complete documentation of what BaseUserRepository provides, see:
 * docs/STARTER_REFERENCE.md#baseuserrepository
 *
 * Add custom query methods here if needed.
 */
@Repository
public interface UserRepository extends BaseUserRepository<User> {

    // TODO: Add custom query methods here if needed

}
