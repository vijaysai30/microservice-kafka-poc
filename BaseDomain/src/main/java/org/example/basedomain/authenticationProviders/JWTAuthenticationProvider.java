package org.example.basedomain.authenticationProviders;

import org.example.basedomain.Service.UserService;
import org.example.basedomain.token.JwtAuthenticationToken;
import org.example.basedomain.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


public class JWTAuthenticationProvider implements AuthenticationProvider {
    private JWTUtil jwtUtil;
    private UserService userService;


    public JWTAuthenticationProvider(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((JwtAuthenticationToken) authentication).getToken();
        String userName = jwtUtil.validateAndExtractUserNameFromToken(token);
        if (userName == null) {
            return null;
        }
        UserDetails userDetails = userService.loadUserByUsername(userName);
        if (userDetails == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
