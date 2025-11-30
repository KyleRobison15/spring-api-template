// TODO: Rename package to match your organization (e.g., com.yourcompany.yourapp.users)
package com.krd.api.users;

import com.krd.starter.user.BaseUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * User entity extending BaseUser from the spring-api-starter.
 *
 * For complete documentation of what BaseUser provides, see:
 * docs/STARTER_REFERENCE.md#baseuser-entity
 *
 * Add your custom fields below this class.
 *
 * IMPORTANT: Use @SuperBuilder instead of @Builder when extending BaseUser.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
public class User extends BaseUser {

    // TODO: Add your custom fields here
    // Example:
    // @Column(name = "phone_number")
    // private String phoneNumber;

}
