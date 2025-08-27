package com.descenedigital.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, IOException {

    String requestURI = request.getRequestURI();

    System.out.println("=== JWT FILTER START ===");
    System.out.println("Request URI: " + requestURI);
    System.out.println("Authorization Header: " + request.getHeader("Authorization"));

    if (requestURI.startsWith("/api/auth") ||
        requestURI.startsWith("/swagger-ui") ||
        requestURI.startsWith("/v3/api-docs") ||  
        requestURI.startsWith("/swagger-resources") ||
        requestURI.startsWith("/webjars") ||
        requestURI.startsWith("/h2-console") ||
        requestURI.startsWith("/api/debug")) {

        System.out.println("Skipping JWT processing for public endpoint");
        filterChain.doFilter(request, response);
        return;
    }

    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        System.out.println("No Bearer token found!, sending 401 error");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"JWT token is required\"}");
        return;
    }

    try {
        final String jwt = authHeader.substring(7);
        final String username = jwtUtils.extractUsername(jwt);

        System.out.println("Extracted username: " + username);
        System.out.println("JWT Token (first 20 chars): " +
                (jwt.length() > 20 ? jwt.substring(0, 20) + "..." : jwt));

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Loading user details for: " + username);

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            System.out.println("User authorities: " + userDetails.getAuthorities());

            if (jwtUtils.validateToken(jwt, userDetails)) {
                System.out.println("✅ Token is valid");

                List<String> roles = jwtUtils.extractRoles(jwt);
                System.out.println("Roles from token: " + roles);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("✅ Authentication set in SecurityContext");
            } else {
                System.out.println("❌ Token validation failed");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid or expired token\"}");
                return;
            }
        }

    } catch (Exception e) {
        System.err.println("❌ JWT Error: " + e.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid token: " + e.getMessage() + "\"}");
        return;
    }

    System.out.println("=== JWT FILTER END ===");
    filterChain.doFilter(request, response);
}

}