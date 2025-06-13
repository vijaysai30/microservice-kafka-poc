package org.example.orderservice.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.basedomain.Dto.UserDetailsDTO;
import org.example.orderservice.service.UserClient;
import org.example.orderservice.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JWTValidationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserClient userClient;


    public JWTValidationFilter(JWTUtil jwtUtil, UserClient userClient) {
        this.jwtUtil = jwtUtil;
        this.userClient = userClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extracttokenFromRequest(request);
        if (token != null) {
            String username = jwtUtil.validateAndExtractUserNameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetailsDTO userDetails = userClient.getUserInfo(username);
                GrantedAuthority authority = new SimpleGrantedAuthority(userDetails.getRole());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, List.of(authority));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    public String extracttokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

