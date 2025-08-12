package com.descenedigital.controller;

import com.descenedigital.dto.AdviceDTO;
import com.descenedigital.dto.CreateAdviceRequest;
import com.descenedigital.dto.RateRequest;
import com.descenedigital.model.Advice;
import com.descenedigital.model.Rating;
import com.descenedigital.repo.UserRepository;
import com.descenedigital.service.AdviceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/advice")
public class AdviceController {


    private final AdviceService adviceService;
    private final UserRepository userRepository;

    public AdviceController(AdviceService adviceService, UserRepository userRepository) {
        this.adviceService = adviceService;
        this.userRepository = userRepository;
    }

    private AdviceDTO toDto(Advice a) {
        AdviceDTO d = new AdviceDTO();
        d.id = a.getId(); d.title = a.getTitle(); d.content = a.getContent(); d.author = a.getAuthor(); d.createdAt = a.getCreatedAt(); d.updatedAt = a.getUpdatedAt();
        var avg = a.getRatings().stream().mapToInt(Rating::getValue).average();
        d.averageRating = avg.isPresent() ? avg.getAsDouble() : null;
        return d;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateAdviceRequest req, Authentication auth) {
        // Only ADMIN may create — check authority
        if (auth == null || auth.getAuthorities().stream().noneMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        var created = adviceService.create(req, auth.getName());
        return ResponseEntity.ok(toDto(created));
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(required = false) String q, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable p = PageRequest.of(page, size);
        Page<Advice> res = adviceService.search(q, p);
        Page<AdviceDTO> dto = new PageImpl<>(res.stream().map(this::toDto).collect(Collectors.toList()), p, res.getTotalElements());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<?> topRated(@RequestParam(defaultValue = "5") int limit) {
        var page = adviceService.topRated(PageRequest.of(0, limit));
        var dto = page.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Advice a = adviceService.get(id);
        return ResponseEntity.ok(toDto(a));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CreateAdviceRequest req, Authentication auth) {
        if (auth == null || auth.getAuthorities().stream().noneMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        var updated = adviceService.update(id, req);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        if (auth == null || auth.getAuthorities().stream().noneMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        adviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rate(@PathVariable Long id, @Valid @RequestBody RateRequest req, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        var user = userRepository.findByUsername(auth.getName()).orElseThrow();
        Rating r = adviceService.rate(id, user, req.value);
        return ResponseEntity.ok(Map.of("ratingId", r.getId(), "value", r.getValue()));
    }

}