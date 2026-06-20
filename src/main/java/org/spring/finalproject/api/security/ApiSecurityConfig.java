package org.spring.finalproject.api.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@ConditionalOnProperty(name = "app.jwt.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class ApiSecurityConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {

        return new JwtAuthFilter(jwtService, userDetailsService);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter) throws Exception {

        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/orders/*/approve")
                        .hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/v1/**")
                        .authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeJsonError(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden")))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private static void writeJsonError(
            HttpServletResponse response,
            int status,
            String message) throws java.io.IOException {

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"status\":" + status + ",\"message\":\"" + message + "\"}");
    }
}
