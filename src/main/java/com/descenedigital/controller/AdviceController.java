package com.descenedigital.controller;

import com.descenedigital.model.Advice;
import com.descenedigital.service.AdviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(adviceService.createAdvice(advice), HttpStatus.CREATED);
    }

    //adm
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<Advice>> updateAdvice(@PathVariable Long id,
                                                         @RequestBody Advice updatedAdvice) {
        return new ResponseEntity<>(adviceService.updateAdvice(id, updatedAdvice), HttpStatus.OK);
    }

    //adm
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAdvice(@PathVariable Long id) {
        boolean deleted = adviceService.deleteAdvice(id);
        return deleted
                ?  new ResponseEntity<>("Advice deleted", HttpStatus.OK)
                :  new ResponseEntity<>("Advice not deleted", HttpStatus.NOT_FOUND);
    }

    //usr
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<Advice>> getAllAdvice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return new ResponseEntity<>(adviceService.getAllAdvice(PageRequest.of(page, size)), HttpStatus.OK);
    }

    //usr
    @PostMapping("/{id}/rate")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Optional<Advice>> rateAdvice(@PathVariable Long id,
                                                       @RequestParam double rating) {
        return new ResponseEntity<>(adviceService.rateAdvice(id, rating), HttpStatus.OK);
    }

    //usr
    @GetMapping("/getTop")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<Advice>> getTopAdvice() {
        return new ResponseEntity<>(adviceService.getTopRatedAdvice(PageRequest.of(0, 5)), HttpStatus.OK);
    }
}
