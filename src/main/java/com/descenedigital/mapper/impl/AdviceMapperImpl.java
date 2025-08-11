package com.descenedigital.mapper.impl;

import com.descenedigital.dto.AdviceDto;
import com.descenedigital.mapper.AdviceMapper;
import com.descenedigital.model.Advice;
import org.springframework.stereotype.Component;

@Component
public class AdviceMapperImpl implements AdviceMapper {

    @Override
    public Advice fromDto(AdviceDto adviceDto) {
        return new Advice(
                adviceDto.id(),
                adviceDto.message(),
                null,
                null,
                0,
                0
        );
    }

    @Override
    public AdviceDto toDto(Advice advice) {
        double average = advice.getRatingCount() == 0 ? 0 : (double) advice.getRatingSum() / advice.getRatingCount();
        return new AdviceDto(
                advice.getId(),
                advice.getMessage(),
                advice.getRatingSum(),
                advice.getRatingCount(),
                average
        );
    }
}
