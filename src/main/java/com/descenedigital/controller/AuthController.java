package com.descenedigital.controller;

import com.descenedigital.dto.JwtResponse;
import com.descenedigital.dto.LoginRequest;
import com.descenedigital.dto.RegisterRequest;
import com.descenedigital.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {
  private final AuthService svc;
  public AuthController(AuthService svc){ this.svc = svc; }

  @Operation(summary = "Register a new user")
  @ApiResponse(responseCode = "200", description = "Registered")
  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest r){
    svc.register(r);
    return ResponseEntity.ok().build();
  }
  @Operation(summary = "Login and get JWT")
  @ApiResponses(@ApiResponse(responseCode = "200",
          content = @Content(schema = @Schema(implementation = JwtResponse.class))))
  @PostMapping("/login")
  public JwtResponse login(@RequestBody @Valid LoginRequest r){
    return svc.login(r);
  }
}
