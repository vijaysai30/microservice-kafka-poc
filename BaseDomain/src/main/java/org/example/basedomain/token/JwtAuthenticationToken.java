package org.example.basedomain.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    public final String token;
    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false);
    }
    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
    @Override
    public Object getPrincipal() {
        return null;
    }

}
