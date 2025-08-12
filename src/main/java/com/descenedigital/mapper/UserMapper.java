package com.descenedigital.mapper;

import com.descenedigital.dto.auth.RegisterRequest;
import com.descenedigital.dto.user.UserResponse;
import com.descenedigital.model.Role;
import com.descenedigital.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "adviceList", ignore = true)
    User toEntity(RegisterRequest request);
    
    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToStrings")
    UserResponse toResponse(User user);
    
    @org.mapstruct.Named("rolesToStrings")
    default Set<String> rolesToStrings(Set<Role> roles) {
        return roles.stream()
                .map(Role::getValue)
                .collect(Collectors.toSet());
    }
}
