package org.example.basedomain.Security;

import org.example.basedomain.Service.UserService;
import org.example.basedomain.authenticationProviders.JWTAuthenticationProvider;
import org.example.basedomain.exception.OrderServiceAccessDeniedHandler;
import org.example.basedomain.exception.OrderServiceAuthEntryPoint;
import org.example.basedomain.filters.JWTAuthenticationFilter;
import org.example.basedomain.filters.JWTRefreshTokenFilter;
import org.example.basedomain.filters.JWTValidationFilter;
import org.example.basedomain.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig  {
    private JWTUtil jwtUtil;
    private UserService userService;

    @Autowired
    public SecurityConfig(JWTUtil  jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }
    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider() {
        return new JWTAuthenticationProvider(jwtUtil, userService);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(daoAuthenticationProvider(), jwtAuthenticationProvider()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity htpp, AuthenticationManager authenticationManager, JWTUtil jwtUtil) throws Exception {

       // first calls JWT authencation filter to check user on first login and return token in response
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager,jwtUtil);

        // to validate token
        JWTValidationFilter jwtValidationFilter = new JWTValidationFilter(authenticationManager);

        // to refresh token
        JWTRefreshTokenFilter jwtRefreshTokenFilter = new JWTRefreshTokenFilter(authenticationManager,jwtUtil);

        htpp.authorizeHttpRequests(auth->auth
                .requestMatchers("auth/create","auth/login","auth/info").permitAll()
                .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                .accessDeniedHandler(new OrderServiceAccessDeniedHandler())
                .authenticationEntryPoint(new OrderServiceAuthEntryPoint())
                )
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf->csrf.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidationFilter,JWTAuthenticationFilter.class )
                .addFilterAfter(jwtRefreshTokenFilter,JWTValidationFilter.class);

        return htpp.build();
    }

}

