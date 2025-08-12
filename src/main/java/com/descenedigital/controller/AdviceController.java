package com.descenedigital.controller;

import com.descenedigital.dto.AdviceDto;
import com.descenedigital.mapper.AdviceMapper;
import com.descenedigital.model.Advice;
import com.descenedigital.service.AdviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Advice API", description = "API for managing advice entries")
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
    @Operation(summary = "Get advice by ID", description = "Fetch a single advice by its ID")
    @GetMapping("/{advice_id}")
    public AdviceDto getAdviceById(
            @Parameter(description = "ID of the advice to retrieve", required = true)
            @PathVariable("advice_id") Long id){
        return adviceMapper.toDto(adviceService.getAdviceById(id));
    }

    // Get all advices with pagination and sorting
    @Operation(summary = "Get all advices with pagination and sorting", description = "Fetch paginated and optionally filtered advices")
    @GetMapping()
    public Page<AdviceDto> getAdvice(
            @Parameter(description = "Keyword to filter advices by message")
            @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "created", direction = Sort.Direction.DESC)
            }) Pageable pageable) {
        return adviceService.getAdviceFiltered(keyword, pageable)
                .map(adviceMapper::toDto);
    }

    // Post an advice
    @Operation(summary = "Create a new advice", description = "Add a new advice to the system")
    @PostMapping()
    public AdviceDto createAdvice(
            @Valid @RequestBody AdviceDto adviceDto){
        Advice createdAdvice = adviceService.CreateWorkout(adviceMapper.fromDto(adviceDto));
        return adviceMapper.toDto(createdAdvice);
    }

    // Delete an advice
    @Operation(summary = "Delete an advice", description = "Delete an advice by its ID")
    @DeleteMapping(path = "/{advice_id}")
    public void  deleteAdvice(
            @Parameter(description = "ID of the advice to delete", required = true)
            @PathVariable("advice_id") Long id){
        adviceService.DeleteAdvice(id);
    }

    // Update an advice workout
    @Operation(summary = "Update an existing advice", description = "Update advice details by ID")
    @PutMapping(path = "/{advice_id}")
    public AdviceDto updatedAdvice(
            @Parameter(description = "ID of the advice to update", required = true)
            @PathVariable("advice_id")Long id,
            @RequestBody AdviceDto adviceDto
    ){
        Advice updatedAdvice=  adviceService.updateAdvice(id,adviceMapper
                .fromDto(adviceDto));
        return adviceMapper.toDto(updatedAdvice);
    }


}