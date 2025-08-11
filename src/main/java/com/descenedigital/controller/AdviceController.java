package com.descenedigital.controller;

import com.descenedigital.model.Advice;
import com.descenedigital.service.AdviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/advice")
@RequiredArgsConstructor
public class AdviceController {

    private final AdviceService adviceService;

    //adm
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Advice> createAdvice(@RequestBody Advice advice) {
        return ResponseEntity.ok(adviceService.createAdvice(advice));
    }

    //adm
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<Advice>> updateAdvice(@PathVariable Long id,
                                                         @RequestBody Advice updatedAdvice) {
        return ResponseEntity.ok(adviceService.updateAdvice(id, updatedAdvice));
    }

    //adm
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAdvice(@PathVariable Long id) {
        boolean deleted = adviceService.deleteAdvice(id);
        return deleted
                ? ResponseEntity.ok("Advice deleted successfully.")
                : ResponseEntity.status(404).body("Advice not found.");
    }

    //usr
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<Advice>> getAllAdvice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(adviceService.getAllAdvice(PageRequest.of(page, size)));
    }

    //usr
    @PostMapping("/{id}/rate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Optional<Advice>> rateAdvice(@PathVariable Long id,
                                                       @RequestParam double rating) {
        return ResponseEntity.ok(adviceService.rateAdvice(id, rating));
    }

    //usr
    @GetMapping("/getTop")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<Advice>> getTopAdvice() {
        return ResponseEntity.ok(adviceService.getTopRatedAdvice(PageRequest.of(0, 5)));
    }
}
