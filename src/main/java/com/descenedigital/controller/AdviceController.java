package com.descenedigital.controller;

import com.descenedigital.dto.AdviceDto;
import com.descenedigital.dto.RatingDto;
import com.descenedigital.service.AdviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/advice")
@RequiredArgsConstructor
public class AdviceController {
    private final AdviceService adviceService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public Page<AdviceDto> getAllAdvice(@PageableDefault(size = 10) Pageable pageable){

        return adviceService.getAllAdvice(pageable);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AdviceDto> getAdvicebyId(@PathVariable Long id){

        return adviceService.getAdvicebyId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AdviceDto> createAdvice(@Valid @RequestBody AdviceDto request, UriComponentsBuilder uriComponentsBuilder){
        return adviceService.createAdvice(request, uriComponentsBuilder);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AdviceDto> updateAdvice(@Valid @RequestBody AdviceDto request, @PathVariable Long id){
        return adviceService.updateAdvice(request,id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvice(@PathVariable Long id){
        return adviceService.deleteAdvice(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}/rating")
    public ResponseEntity<AdviceDto> addRating(@Valid @RequestBody RatingDto ratingDto, @PathVariable Long id){
        return adviceService.addRating(ratingDto, id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/toprating")
    public ResponseEntity<?> getTopRating(@RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size){

        return adviceService.getTopRating(page, size);
    }








}