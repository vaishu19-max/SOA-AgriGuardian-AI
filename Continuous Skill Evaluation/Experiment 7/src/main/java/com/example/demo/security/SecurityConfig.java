package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication
    .UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/token").permitAll()
                .requestMatchers(
                    org.springframework.http.HttpMethod.GET,
                    "/books", "/books/**"
                ).authenticated()
                .requestMatchers(
                    org.springframework.http.HttpMethod.POST,
                    "/books", "/books/**"
                ).hasAnyRole("ADMIN", "LIBRARIAN")
                .requestMatchers(
                    org.springframework.http.HttpMethod.PUT,
                    "/books/**"
                ).hasAnyRole("ADMIN", "LIBRARIAN")
                .requestMatchers(
                    org.springframework.http.HttpMethod.DELETE,
                    "/books/**"
                ).hasAnyRole("ADMIN", "LIBRARIAN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}