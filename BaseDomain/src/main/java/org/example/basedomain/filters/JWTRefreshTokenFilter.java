package org.example.basedomain.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.basedomain.token.JwtAuthenticationToken;
import org.example.basedomain.util.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTRefreshTokenFilter extends OncePerRequestFilter {

    private  AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;

    public JWTRefreshTokenFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(!request.getServletPath().equals("/refreshToken")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = extractTokenFromRequest(request);
        if(token==null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
        Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);
        if(authentication.isAuthenticated()){
           String tokenRefreshed= jwtUtil.genrateToken(authentication.getName(), 15);
           response.setHeader("Authorization", "Bearer "+tokenRefreshed);
        }
    }
    public String extractTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            return null;
        }
        for(Cookie cookie : request.getCookies()){
            if(cookie.getName().equals("refreshToken")){
                return cookie.getValue();
            }
        }
        return null;
    }

}
