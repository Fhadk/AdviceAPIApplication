package com.descenedigital.controller;

import com.descenedigital.dto.AdviceDto;
import com.descenedigital.mapper.AdviceMapper;
import com.descenedigital.model.Advice;
import com.descenedigital.service.AdviceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advice")
public class AdviceController {

    private final AdviceMapper adviceMapper;
    private final AdviceService adviceService;

    public  AdviceController(AdviceMapper adviceMapper,AdviceService adviceService){
        this.adviceMapper = adviceMapper;
        this.adviceService = adviceService;
    }

    // Get a single advice
    @GetMapping("/{advice_id}")
    public AdviceDto getAdviceById(@PathVariable("advice_id") Long id){
        return adviceMapper.toDto(adviceService.getAdviceById(id));
    }
    // Get all advices
    @GetMapping()
    public List<AdviceDto> ListOfAdvices(){
        return adviceService.ListOfAdvices()
                .stream()
                .map(adviceMapper::toDto)
                .toList();
    }

    // Post an advice
    @PostMapping()
    public AdviceDto createAdvice(@Valid @RequestBody AdviceDto adviceDto){
        Advice createdAdvice = adviceService.CreateWorkout(adviceMapper.fromDto(adviceDto));
        return adviceMapper.toDto(createdAdvice);
    }

    // Delete an advice
    @DeleteMapping(path = "/{advice_id}")
    public void  deleteAdvice(@PathVariable("advice_id") Long id){
        adviceService.DeleteAdvice(id);
    }

    // Update an advice workout
    @PutMapping(path = "/{advice_id}")
    public AdviceDto updatedAdvice(
            @PathVariable("advice_id")Long id,
            @RequestBody AdviceDto adviceDto
    ){
        Advice updatedAdvice=  adviceService.updateAdvice(id,adviceMapper
                .fromDto(adviceDto));
        return adviceMapper.toDto(updatedAdvice);
    }

}