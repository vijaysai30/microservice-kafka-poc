package org.example.apigateway.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JWTUtil {

    public static final String SECRET_KEY="your-secure-secret-key-min-32bytes";
    public static final Key key= Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        }
        catch (ExpiredJwtException e) {
            throw new JwtException("Token has expired");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Token is not supported");
        } catch (MalformedJwtException e) {
            throw new JwtException("Token is malformed");
        } catch (SignatureException e) {
            throw new JwtException("Invalid signature");
        } catch (IllegalArgumentException e) {
            throw new JwtException("Token is null or empty");
        }
//        return false;
    }
}
