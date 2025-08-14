package com.descenedigital.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import com.descenedigital.model.*;
import com.descenedigital.repo.*;
import com.descendigital.enums.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/advice")
@RequiredArgsConstructor
@Tag(name = "Advice Management", description = "Endpoints for managing advice and ratings")
@SecurityRequirement(name = "bearerAuth")
public class AdviceController {
    private final AdviceRepository adviceRepository;
    private final UserRepository userRepository;

    public AdviceController(AdviceRepository adviceRepository, UserRepository userRepository) {
		super();
		this.adviceRepository = adviceRepository;
		this.userRepository = userRepository;
	}

	@GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all advice", description = "Retrieve a list of all advice entries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public List<Advice> getAllAdvice() {
        return adviceRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get advice by ID", description = "Retrieve a specific advice entry by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved advice"),
            @ApiResponse(responseCode = "404", description = "Advice not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public Advice getAdviceById(
            @Parameter(description = "ID of advice to be retrieved") @PathVariable Long id) {
        return adviceRepository.findById(id).orElseThrow();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new advice", description = "Create a new advice entry (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created advice"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public Advice createAdvice(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        
        Advice advice = new Advice();
        advice.setMessage(request.get("message"));
        advice.setUser(user);
        
        return adviceRepository.save(advice);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update advice", description = "Update an existing advice entry (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated advice"),
            @ApiResponse(responseCode = "404", description = "Advice not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public Advice updateAdvice(
            @Parameter(description = "ID of advice to be updated") @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        Advice advice = adviceRepository.findById(id).orElseThrow();
        advice.setMessage(request.get("message"));
        return adviceRepository.save(advice);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete advice", description = "Delete an advice entry (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted advice"),
            @ApiResponse(responseCode = "404", description = "Advice not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public Map<String, String> deleteAdvice(
            @Parameter(description = "ID of advice to be deleted") @PathVariable Long id) {
        adviceRepository.deleteById(id);
        return Map.of("message", "Advice deleted successfully");
    }

    @PostMapping("/{id}/rate")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Rate advice", description = "Rate an advice entry (Users and Admins)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully rated advice"),
            @ApiResponse(responseCode = "400", description = "Invalid rating or self-rating by admin"),
            @ApiResponse(responseCode = "404", description = "Advice not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public Rating rateAdvice(
            @Parameter(description = "ID of advice to be rated") @PathVariable Long id,
            @RequestBody Map<String, Integer> request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Advice advice = adviceRepository.findById(id).orElseThrow();

        if (user.getRole() == Role.ROLE_ADMIN && 
            advice.getUser() != null && 
            advice.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Admins cannot rate their own advice");
        }

        Rating rating = new Rating();
        rating.setScore(request.get("score"));
        rating.setUser(user);
        rating.setAdvice(advice);
        
        advice.getRatings().add(rating);
        adviceRepository.save(advice);
        
        return rating;
    }

    @GetMapping("/top-rated")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get top rated advice", description = "Retrieve top 10 highest rated advice entries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved top rated advice"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public List<Advice> getTopRatedAdvice() {
        return adviceRepository.findAll().stream()
                .filter(a -> a.getRatings() != null && !a.getRatings().isEmpty())
                .sorted((a1, a2) -> {
                    double avg1 = a1.getRatings().stream()
                            .mapToInt(Rating::getScore)
                            .average()
                            .orElse(0.0);
                    double avg2 = a2.getRatings().stream()
                            .mapToInt(Rating::getScore)
                            .average()
                            .orElse(0.0);
                    return Double.compare(avg2, avg1);
                })
                .limit(10)
                .toList();
    }
}