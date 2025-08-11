package com.descenedigital.mapper;

import com.descenedigital.dto.AuthRequest;
import com.descenedigital.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(AuthRequest dto);
}
