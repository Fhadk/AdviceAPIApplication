package com.descenedigital.service;

import com.descenedigital.dto.RatingDto;
import com.descenedigital.model.Advice;
import com.descenedigital.dto.AdviceDto;
import com.descenedigital.mappers.AdviceMapper;
import com.descenedigital.repo.AdviceRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@AllArgsConstructor
public class AdviceService {
    private final AdviceRepo adviceRepo;
    public AdviceMapper adviceMapper;

    public Page<AdviceDto> getAllAdvice(Pageable pageable){
        Page<Advice> advicePage = adviceRepo.findAll(pageable);
        return advicePage.map(adviceMapper::toDto);

    }


    public ResponseEntity<AdviceDto> getAdvicebyId(Long id) {
        var response = adviceRepo.findById(id).orElse(null);
        if (response== null){
            return ResponseEntity.notFound().build();
        }
        var adviceDto = adviceMapper.toDto(response);
        return ResponseEntity.ok(adviceDto);
    }

    public ResponseEntity<AdviceDto> createAdvice(AdviceDto request, UriComponentsBuilder uriComponentsBuilder){
        Advice advice = adviceMapper.toEntity(request);
        Double rating = advice.getRating();
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be between 1..5");

        adviceRepo.save(advice);
        AdviceDto adviceDto = adviceMapper.toDto(advice);
        URI uri = uriComponentsBuilder.path("/advice/{id}").buildAndExpand(advice.getId()).toUri();
        return ResponseEntity.created(uri).body(adviceDto);
    }

    public ResponseEntity<AdviceDto> updateAdvice(AdviceDto request, Long id) {
        Advice response = adviceRepo.findById(id).orElse(null);

        if (response == null){
            return ResponseEntity.notFound().build();
        }
        Double rating = response.getRating();
        if (rating < 1 || rating > 5){
            //Rating must be between 1 and 5
            return ResponseEntity.badRequest().build();
        }
        adviceMapper.updateAdviceRequest(request, response);
        adviceRepo.save(response);
        return ResponseEntity.ok().body(request);

    }
    public ResponseEntity<Void> deleteAdvice(Long id){
        Advice response = adviceRepo.findById(id).orElse(null);
        if(response == null){
            return ResponseEntity.notFound().build();
        }
        adviceRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<AdviceDto> addRating(RatingDto ratingDto, Long id) {
        Advice response = adviceRepo.findById(id).orElse(null);
        Double rating = ratingDto.getRating();
        if (rating < 1 || rating > 5){
            //Rating must be between 1 and 5
            return ResponseEntity.badRequest().build();
        }
        if (response == null){
            return ResponseEntity.notFound().build();
        }

        response.setRating(rating);
        adviceRepo.save(response);

        return ResponseEntity.ok().body(adviceMapper.toDto(response));

    }

    public ResponseEntity<?> getTopRating(int page,int size) {
        Pageable p = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "Rating"));
        var response = adviceRepo.findAll(p).map(adviceMapper:: toDto);
        return ResponseEntity.ok().body(response);

    }



}