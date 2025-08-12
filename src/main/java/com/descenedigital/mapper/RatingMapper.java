package com.descenedigital.mapper;

import com.descenedigital.dto.rating.RatingRequest;
import com.descenedigital.dto.rating.RatingResponse;
import com.descenedigital.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RatingMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "advice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Rating toEntity(RatingRequest request);
    
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "advice.id", target = "adviceId")
    @Mapping(source = "advice.title", target = "adviceTitle")
    RatingResponse toResponse(Rating rating);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "advice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(RatingRequest request, @MappingTarget Rating rating);
}
