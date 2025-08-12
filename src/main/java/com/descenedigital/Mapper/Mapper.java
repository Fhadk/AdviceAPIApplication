package com.descenedigital.Mapper;

import com.descenedigital.dto.AdviceResp;
import com.descenedigital.entity.Advice;

public class Mapper {
  public static AdviceResp toResp(Advice a){
    return new AdviceResp(
      a.getId(), a.getMessage(),
      a.getCreatedBy()!=null ? a.getCreatedBy().getUsername() : null,
      a.getAvgRating(), a.getRatingsCount(), a.getCreatedAt()
    );
  }
}