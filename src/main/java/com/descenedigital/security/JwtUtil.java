package com.descenedigital.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component

public class JwtUtil {
    private final String secret = "P7AqhJq4t8bZxLWmcRjVK8YzyJw7DgkThVgFlpRkCqs=";
    private final long minutes = 60;

    public String generate(String username, Collection<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(minutes, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public String subject(String token) {
        return parse(token).getBody().getSubject();
    }

    public List<String> roles(String token) {
        Object r = parse(token).getBody().get("roles");
        if (r instanceof List<?> list) {
            return list.stream().map(Object::toString).collect(Collectors.toList());
        }
        return List.of();
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token);
    }
}
