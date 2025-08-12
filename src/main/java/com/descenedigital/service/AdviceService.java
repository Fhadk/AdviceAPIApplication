package com.descenedigital.service;


import com.descenedigital.Mapper.Mapper;
import com.descenedigital.dto.AdviceCreateReq;
import com.descenedigital.dto.AdviceResp;
import com.descenedigital.dto.AdviceUpdateReq;
import com.descenedigital.dto.PageResp;
import com.descenedigital.entity.Advice;
import com.descenedigital.entity.User;
import com.descenedigital.repo.AdviceRatingRepo;
import com.descenedigital.repo.AdviceRepo;
import com.descenedigital.repo.UserRepo;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AdviceService {
    private final AdviceRepo repo;
    private final UserRepo users;
    private final AdviceRatingRepo ratings;
    public AdviceService(AdviceRepo r, UserRepo u, AdviceRatingRepo ratings){ repo=r; users=u;
        this.ratings = ratings;
    }

    public AdviceResp create(AdviceCreateReq req, Authentication auth){
        User creator = users.findByUsername(auth.getName()).orElse(null);
        var a = Advice.builder().message(req.message()).createdBy(creator).build();
        return Mapper.toResp(repo.save(a));
    }

    public AdviceResp update(Long id, AdviceUpdateReq req){
        var a = repo.findById(id).orElseThrow();
        a.setMessage(req.message());
        return Mapper.toResp(repo.save(a));
    }

    public void delete(Long id){
        ratings.deleteById(id);
        repo.deleteById(id); }

    public PageResp<AdviceResp> list(String search, Pageable pageable){
        var p = (search==null || search.isBlank()) ? repo.findAll(pageable) : repo.findAllByMessageContainingIgnoreCase(search, pageable);
        return PageResp.of(p.map(Mapper::toResp));
    }

    public PageResp<AdviceResp> top(Pageable pageable){
        return PageResp.of(repo.topRated(pageable).map(Mapper::toResp));
    }
}
