// TODO: Rename package to match your organization (e.g., com.yourcompany.yourapp.users)
package com.krd.api.users;

import com.krd.starter.user.BaseUserMapper;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between User entities and UserDto objects.
 *
 * For complete documentation of what BaseUserMapper provides, see:
 * docs/STARTER_REFERENCE.md#baseusermapper
 *
 * MapStruct will automatically generate implementation at compile time.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends BaseUserMapper<User, UserDto> {

    // MapStruct generates implementation automatically
    // TODO: Add custom mapping methods here if needed

}
