package com.descenedigital.controller;
import com.descenedigital.model.Advice;
import com.descenedigital.model.User;
import com.descenedigital.service.AdviceService;
import java.util.*;
import com.descenedigital.service.UserService;
import com.descendigital.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/advice")
@RequiredArgsConstructor
public class AdviceController {

    public AdviceController(AdviceService adviceService, UserService userService) {
		super();
		this.adviceService = adviceService;
		this.userService = userService;
	}

	private final AdviceService adviceService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createAdvice(@RequestBody Advice advice, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName());
            advice.setUser(user);
            Advice savedAdvice = adviceService.save(advice);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Advice created successfully",
                "data", savedAdvice
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Bad Request",
                "message", "Failed to create advice: " + e.getMessage(),
                "status", 400
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Advice> updateAdvice(@PathVariable Long id, @RequestBody Advice advice) {
        return ResponseEntity.ok(adviceService.update(id, advice));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> deleteAdvice(@PathVariable Long id) {
        adviceService.delete(id);
        return Map.of("message", "Advice deleted successfully");
    }


    @GetMapping
    public ResponseEntity<List<Advice>> getAllAdvice() {
        return ResponseEntity.ok(adviceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Advice> getAdviceById(@PathVariable Long id) {
        return ResponseEntity.ok(adviceService.findById(id));
    }
    
    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rateAdvice(
            @PathVariable Long id,
            @RequestParam int score,
            Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());
        if (user.getRole() == Role.ROLE_ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                        "error", "Forbidden",
                        "message", "Admins cannot rate advices. Only users can rate advices.",
                        "status", 403
                    ));
        }
   
        if (score < 1 || score > 5) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                        "error", "Bad Request",
                        "message", "Rating score must be between 1 and 5",
                        "status", 400
                    ));
        }
        
        try {
            Advice ratedAdvice = adviceService.rateAdvice(id, user, score);
            return ResponseEntity.ok(ratedAdvice);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "error", "Not Found",
                        "message", e.getMessage(),
                        "status", 404
                    ));
        }
    }
    @GetMapping("/top-rated")
    public ResponseEntity<List<Advice>> getTopRatedAdvices() {
        return ResponseEntity.ok(adviceService.getTopRatedAdvices());
    }

    @GetMapping("/low-rated")
    public ResponseEntity<List<Advice>> getLowRatedAdvices() {
        return ResponseEntity.ok(adviceService.getLowRatedAdvices());
    }
}
