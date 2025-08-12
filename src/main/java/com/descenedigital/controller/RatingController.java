package com.descenedigital.controller;


import com.descenedigital.dto.RateReq;
import com.descenedigital.dto.RatingResp;
import com.descenedigital.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
  private final RatingService svc;
  public RatingController(RatingService s){ this.svc = s; }

  @PostMapping("/{adviceId}")
  public RatingResp rate(@PathVariable Long adviceId, @RequestBody @Valid RateReq req, Authentication auth){
    return svc.rate(adviceId, auth.getName(), req);
  }
}
