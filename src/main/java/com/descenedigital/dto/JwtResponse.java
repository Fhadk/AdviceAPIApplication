package com.descenedigital.dto;
public record JwtResponse(String accessToken, String tokenType) {
  public JwtResponse(String token){ this(token,"Bearer"); }
}
