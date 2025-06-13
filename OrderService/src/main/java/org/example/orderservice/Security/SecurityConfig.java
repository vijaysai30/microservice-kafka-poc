package org.example.orderservice.Security;

import org.example.basedomain.Service.UserService;
import org.example.basedomain.authenticationProviders.JWTAuthenticationProvider;
import org.example.basedomain.filters.JWTAuthenticationFilter;
import org.example.basedomain.filters.JWTRefreshTokenFilter;

import org.example.orderservice.exception.OrderServiceAccessDeniedHandler;
import org.example.orderservice.exception.OrderServiceAuthEntryPoint;
import org.example.orderservice.filters.JWTValidationFilter;
import org.example.orderservice.service.UserClient;
import org.example.orderservice.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    private JWTUtil jwtUtil;
    private UserClient userClient;

    SecurityConfig(JWTUtil jwtUtil, UserClient userClient) {
        this.jwtUtil = jwtUtil;
        this.userClient = userClient;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity htpp) throws Exception {

        // to validate token
        JWTValidationFilter jwtValidationFilter = new JWTValidationFilter(jwtUtil, userClient);

        htpp.authorizeHttpRequests(auth->auth
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(new OrderServiceAccessDeniedHandler())
                        .authenticationEntryPoint(new OrderServiceAuthEntryPoint())
                )
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf->csrf.disable())
                .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);

        return htpp.build();
    }
}

