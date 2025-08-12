package com.descenedigital.controller;
import com.descenedigital.dto.AdviceCreateReq;
import com.descenedigital.dto.AdviceResp;
import com.descenedigital.dto.AdviceUpdateReq;
import com.descenedigital.dto.PageResp;
import com.descenedigital.service.AdviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Advice")
@SecurityRequirement(name = "bearer-jwt")
@RestController
@RequestMapping("/api/advice")
public class AdviceController {
    private final AdviceService svc;
    public AdviceController(AdviceService s){ this.svc = s; }

    @Operation(summary = "Create advice (ADMIN)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AdviceResp create(@RequestBody @Valid AdviceCreateReq req, Authentication auth){
        return svc.create(req, auth);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdviceResp update(@PathVariable Long id, @RequestBody @Valid AdviceUpdateReq req){
        return svc.update(id, req);
    }
    @Transactional
    @Operation(summary = "Delete Advice by Id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id){
        svc.delete(id); }

    @Operation(summary = "List/Search advice (paged)")
    @GetMapping
    public PageResp<AdviceResp> list(
            @RequestParam(required=false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable p = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdAt"));
        return svc.list(q, p);
    }

    @Operation(summary = "Top-rated advice (paged)")
    @GetMapping("/top")
    public PageResp<AdviceResp> top(@RequestParam(defaultValue="0") int page,
                                    @RequestParam(defaultValue="10") int size){
        return svc.top(PageRequest.of(page,size));
    }
}
