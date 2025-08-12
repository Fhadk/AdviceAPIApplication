package com.descenedigital.dto;
import org.springframework.data.domain.Page;
import java.util.List;

public record PageResp<T>(List<T> items, int page, int size, long total) {
  public static <T> PageResp<T> of(Page<T> p){ return new PageResp<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements()); }
}
