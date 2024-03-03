package com.rocky.identityservice.configs;

import com.rocky.identityservice.services.JwtAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthentication jwtAuthentication;

    @Bean
    public SecurityFilterChain filterChainApis(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req ->
                        req.requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/forget/**",
                                        "/api/auth/generateToken", "/api/auth/comments/**",
                                        "/api/auth/postComments/**").permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthentication, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
