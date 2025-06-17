package org.example.apigateway.filter;


import io.jsonwebtoken.JwtException;
import org.example.apigateway.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    RouteValidator routeValidator;

    public AuthenticationFilter() {
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) ->  {
            System.out.println(exchange.getRequest().toString());
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                HttpHeaders headers = exchange.getRequest().getHeaders();
                // Check if the request has an Authorization header
//                if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
//                    throw new RuntimeException("Missing Authorization Header");
//                }
        System.out.println("inside if route validator");
                String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    System.out.println(token);
                    try {
                        boolean validToken = jwtUtil.validateToken(token);
                        if (!validToken) {
                            throw new JwtException("Invalid Token");
                        }
                    } catch (Exception ex) {
                        throw new JwtException("Invalid Token");
                    }
                } else {
                    throw new JwtException("Authorization header is malformed");
                }
            }

            return chain.filter(exchange);
        });
    }


    public static class Config {
    }
}
