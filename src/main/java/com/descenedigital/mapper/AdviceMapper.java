package com.descenedigital.mapper;

import com.descenedigital.dto.AdviceDto;
import com.descenedigital.dto.CreateAdviceRequest;
import com.descenedigital.model.Advice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(config = GlobalMapperConfig.class)
public interface AdviceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Advice toEntity(CreateAdviceRequest dto);

    AdviceDto toDto(Advice entity);

    List<AdviceDto> toDtoList(List<Advice> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(CreateAdviceRequest dto, @MappingTarget Advice entity);
}