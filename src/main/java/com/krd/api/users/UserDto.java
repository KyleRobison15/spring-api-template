// TODO: Rename package to match your organization (e.g., com.yourcompany.yourapp.users)
package com.krd.api.users;

import com.krd.starter.user.dto.BaseUserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * User DTO extending BaseUserDto from the spring-api-starter.
 *
 * For complete documentation of what BaseUserDto provides, see:
 * docs/STARTER_REFERENCE.md#baseuserdto
 *
 * Add your custom fields here to match your User entity.
 *
 * IMPORTANT: Use @SuperBuilder instead of @Builder when extending BaseUserDto.
 */
@Getter
@Setter
@SuperBuilder
public class UserDto extends BaseUserDto {

    // TODO: Add your custom fields here to match your User entity
    // Example:
    // private String phoneNumber;

}
