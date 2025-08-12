package com.descenedigital.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwt;

  public JwtAuthFilter(JwtUtil jwt) {
    this.jwt = jwt;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain chain) throws ServletException, IOException {

    String auth = req.getHeader("Authorization");

    // No token → just continue; endpoints will decide (401/403) later.
    if (auth == null || !auth.startsWith("Bearer ")) {
      chain.doFilter(req, res);
      return;
    }

    String token = auth.substring(7);

    try {
      String username = jwt.subject(token);
      if (username == null || username.isBlank()) {
        write401(res, "Invalid token: subject missing");
        return;
      }

      List<String> roles = jwt.roles(token);
      if (roles == null || roles.isEmpty()) {
        write401(res, "Invalid token: roles missing");
        return;
      }

      var authorities = roles.stream()
              .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
              .map(SimpleGrantedAuthority::new)
              .toList();

      var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
      SecurityContextHolder.getContext().setAuthentication(authentication);

      chain.doFilter(req, res);
    } catch (ExpiredJwtException e) {
      write401(res, "Token expired");
    } catch (JwtException e) {
      write401(res, "Invalid token");
    } catch (Exception e) {
      write500(res, "Authentication error");
    }
  }

  private void write401(HttpServletResponse res, String msg) throws IOException {
    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    res.setContentType("text/plain;charset=UTF-8");
    res.getWriter().write(msg);
  }

  private void write500(HttpServletResponse res, String msg) throws IOException {
    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    res.setContentType("text/plain;charset=UTF-8");
    res.getWriter().write(msg);
  }
}
