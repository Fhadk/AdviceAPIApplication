package com.descenedigital.web.controller;

import com.descenedigital.common.error.NotFoundException;
import com.descenedigital.domain.entity.Advice;
import com.descenedigital.service.AdviceService;
import com.descenedigital.service.RatingService;
import com.descenedigital.web.dto.AdviceDtos;
import com.descenedigital.web.mapper.AdviceMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/advice")
public class AdviceController {
    private static final String ADVICE_NOT_FOUND = "Advice not found";
    private static final String CREATED_AT = "createdAt";
    private final AdviceService adviceService;
    private final RatingService ratingService;
    private final AdviceMapper adviceMapper;

    public AdviceController(AdviceService adviceService, RatingService ratingService, AdviceMapper adviceMapper) {
        this.adviceService = adviceService;
        this.ratingService = ratingService;
        this.adviceMapper = adviceMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdviceDtos.AdviceResponseDTO> get(@PathVariable Long id) {
        Advice advice = adviceService.findById(id).orElseThrow(() -> new NotFoundException(ADVICE_NOT_FOUND));
        AdviceDtos.AdviceResponseDTO dto = adviceMapper.toDto(advice);
        dto.setAverageRating(adviceService.averageRating(advice.getId()));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search")
    public ResponseEntity<AdviceDtos.PageEnvelope<List<AdviceDtos.AdviceResponseDTO>>> search(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) List<String> sort
    ) {
        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        Specification<Advice> spec = buildSpec(q, category, minRating, from, to);
        Page<Advice> result = adviceService.search(spec, pageable);

        List<AdviceDtos.AdviceResponseDTO> content = result.getContent().stream()
                .map(adviceMapper::toDto)
                .toList();
        for (AdviceDtos.AdviceResponseDTO dto : content) {
            dto.setAverageRating(adviceService.averageRating(dto.getId()));
        }

        AdviceDtos.PageEnvelope<List<AdviceDtos.AdviceResponseDTO>> envelope = new AdviceDtos.PageEnvelope<>();
        envelope.setContent(content);
        envelope.setPage(result.getNumber());
        envelope.setSize(result.getSize());
        envelope.setTotalElements(result.getTotalElements());
        envelope.setTotalPages(result.getTotalPages());
        envelope.setSort(sortObj.toString());
        return ResponseEntity.ok(envelope);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<AdviceDtos.PageEnvelope<List<AdviceDtos.AdviceResponseDTO>>> topRated(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(CREATED_AT)));
        Page<Advice> result = adviceService.search((root, query, cb) -> cb.isTrue(root.get("active")), pageable);
        List<AdviceDtos.AdviceResponseDTO> content = result.getContent().stream()
                .map(adviceMapper::toDto)
                .toList();
        for (AdviceDtos.AdviceResponseDTO dto : content) {
            dto.setAverageRating(adviceService.averageRating(dto.getId()));
        }
        content = content.stream()
                .sorted((a, b) -> {
                    int cmp = Double.compare(b.getAverageRating(), a.getAverageRating());
                    if (cmp != 0) return cmp;
                    if (b.getCreatedAt() == null || a.getCreatedAt() == null) return cmp;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .toList();

        AdviceDtos.PageEnvelope<List<AdviceDtos.AdviceResponseDTO>> envelope = new AdviceDtos.PageEnvelope<>();
        envelope.setContent(content);
        envelope.setPage(result.getNumber());
        envelope.setSize(result.getSize());
        envelope.setTotalElements(result.getTotalElements());
        envelope.setTotalPages(result.getTotalPages());
        envelope.setSort("averageRating,DESC;createdAt,DESC");
        return ResponseEntity.ok(envelope);
    }

    @PostMapping
    public ResponseEntity<AdviceDtos.AdviceResponseDTO> create(@Valid @RequestBody AdviceDtos.CreateAdviceDTO request, Authentication auth) {
        Long userId = currentUserId(auth);
        Advice advice = adviceMapper.toEntity(request);
        Advice saved = adviceService.create(advice, userId);
        AdviceDtos.AdviceResponseDTO dto = adviceMapper.toDto(saved);
        dto.setAverageRating(0);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdviceDtos.AdviceResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AdviceDtos.UpdateAdviceDTO request, Authentication auth) {
        Advice advice = adviceService.findById(id).orElseThrow(() -> new NotFoundException(ADVICE_NOT_FOUND));
        Long userId = currentUserId(auth);
        boolean isOwner = advice.getCreatedById().equals(userId);
        boolean isAdmin = hasRole(auth, "ADMIN");
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Forbidden");
        }
        advice.setTitle(request.getTitle());
        advice.setText(request.getText());
        advice.setCategory(request.getCategory());
        Advice updated = adviceService.update(advice);
        AdviceDtos.AdviceResponseDTO dto = adviceMapper.toDto(updated);
        dto.setAverageRating(adviceService.averageRating(id));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        Advice advice = adviceService.findById(id).orElseThrow(() -> new NotFoundException(ADVICE_NOT_FOUND));
        Long userId = currentUserId(auth);
        boolean isOwner = advice.getCreatedById().equals(userId);
        boolean isAdmin = hasRole(auth, "ADMIN");
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Forbidden");
        }
        adviceService.softDelete(advice);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<Void> rate(@PathVariable Long id, @Valid @RequestBody AdviceDtos.RateAdviceDTO request, Authentication auth) {
        Long userId = currentUserId(auth);
        ratingService.rate(id, userId, request.getRating());
        return ResponseEntity.noContent().build();
    }

    private Sort parseSort(List<String> sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.by(Sort.Order.desc(CREATED_AT));
        }
        List<Sort.Order> orders = sort.stream().map(s -> {
            String[] parts = s.split(",");
            String field = parts[0];
            Sort.Direction dir = parts.length > 1 && parts[1].equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            return new Sort.Order(dir, field);
        }).toList();
        return Sort.by(orders);
    }

    private Specification<Advice> buildSpec(String q, String category, Double minRating, String from, String to) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();
            predicates.add(cb.isTrue(root.get("active")));
            if (q != null && !q.isBlank()) {
                var like = "%" + q.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("text")), like)
                ));
            }
            if (category != null && !category.isBlank()) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (from != null && !from.isBlank()) {
                Instant fromTs = LocalDate.parse(from).atStartOfDay().toInstant(ZoneOffset.UTC);
                predicates.add(cb.greaterThanOrEqualTo(root.get(CREATED_AT), fromTs));
            }
            if (to != null && !to.isBlank()) {
                Instant toTs = LocalDate.parse(to).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
                predicates.add(cb.lessThan(root.get(CREATED_AT), toTs));
            }
            if (minRating != null && query != null) {
                var sub = query.subquery(Double.class);
                var ratingRoot = sub.from(com.descenedigital.domain.entity.AdviceRating.class);
                sub.select(cb.avg(ratingRoot.get("rating")))
                        .where(cb.equal(ratingRoot.get("adviceId"), root.get("id")));
                predicates.add(cb.greaterThanOrEqualTo(sub, minRating));
            }
            return cb.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }

    private Long currentUserId(Authentication auth) {
        return Long.parseLong(((String) auth.getPrincipal()).split("\\|")[1]);
    }

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}


