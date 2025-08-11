package com.descenedigital.controller;

import com.descenedigital.dto.*;
import com.descenedigital.mapper.AdviceMapper;
import com.descenedigital.model.Advice;
import com.descenedigital.service.AdviceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/advice")
public class AdviceController {

    private final AdviceService svc;
    private final AdviceMapper mapper;

    public AdviceController(AdviceService svc, AdviceMapper mapper) {
        this.svc = svc;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String keyword,
                                  @RequestParam(defaultValue = "createdAt,desc") String sort) {

        Sort.Order order = parseSort(sort);
        Pageable p = PageRequest.of(page, size, Sort.by(order));
        var pageRes = svc.list(keyword, p).map(mapper::toDto);
        return ResponseEntity.ok(pageRes);
    }

    private Sort.Order parseSort(String sort) {
        String[] s = sort.split(",");
        if (s.length == 2 && s[1].equalsIgnoreCase("asc")) {
            return new Sort.Order(Sort.Direction.ASC, s[0]);
        }
        return new Sort.Order(Sort.Direction.DESC, s[0]);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdviceDto> get(@PathVariable Long id) {
        var a = svc.get(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        return ResponseEntity.ok(mapper.toDto(a));
    }

    @PostMapping
    public ResponseEntity<AdviceDto> create(@Valid @RequestBody CreateAdviceRequest req, Authentication auth) {
        Advice saved = svc.create(req, auth.getName());
        return ResponseEntity.ok(mapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdviceDto> update(@PathVariable Long id, @Valid @RequestBody CreateAdviceRequest req, Authentication auth) {
        Advice updated = svc.update(id, req, auth);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        svc.delete(id, auth);
        return ResponseEntity.ok("deleted");
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<AdviceDto> rate(@PathVariable Long id, @Valid @RequestBody RateRequest r, Authentication auth) {
        Advice rated = svc.rate(id, r.getRating(), auth.getName());
        return ResponseEntity.ok(mapper.toDto(rated));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<?> topRated(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Pageable p = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "averageRating"));
        var pageRes = svc.list(null, p).map(mapper::toDto);
        return ResponseEntity.ok(pageRes);
    }
}
