package org.example.orderservice.util;


//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

//@Component
public class JWTUtil {

//    public static final String SECRET_KEY="your-secure-secret-key-min-32bytes";
//    public static final Key key= Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//
//
//    public String validateAndExtractUserNameFromToken(String token) {
//        try{
//            return Jwts.parser()
//                    .setSigningKey(key)
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .getSubject();
//        }
//        catch (JwtException e){
//            return null;
//        }
//    }
}
