package com.descenedigital.web.mapper;

import com.descenedigital.domain.entity.Advice;
import com.descenedigital.web.dto.AdviceDtos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdviceMapper {
    Advice toEntity(AdviceDtos.CreateAdviceDTO dto);
    Advice toEntity(AdviceDtos.UpdateAdviceDTO dto);

    @Mapping(target = "averageRating", ignore = true)
    AdviceDtos.AdviceResponseDTO toDto(Advice advice);
}


