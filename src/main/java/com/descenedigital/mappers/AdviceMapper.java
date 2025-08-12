package com.descenedigital.mappers;
import com.descenedigital.dto.AdviceDto;
import com.descenedigital.model.Advice;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface AdviceMapper {
    AdviceDto toDto(Advice advice);
    Advice toEntity(AdviceDto adviceDto);
    void updateAdviceRequest(AdviceDto updateAdviceDto, @MappingTarget Advice advice);

}
