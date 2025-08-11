package com.descenedigital.mapper;

import com.descenedigital.dto.AdviceDto;
import com.descenedigital.model.Advice;

public interface AdviceMapper {
    Advice fromDto(AdviceDto adviceDto);
    AdviceDto toDto(Advice advice);
}
