package org.spring.finalproject.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * In-memory users for quick testing when the username is not stored in the database.
     * Usernames: {@code admin}, {@code employee} (not email addresses).
     */
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("Admin123!"))
                .roles(SecurityConstants.ADMIN)
                .build();

        UserDetails employee = User.builder()
                .username("employee")
                .password(encoder.encode("Employee123!"))
                .roles(SecurityConstants.EMPLOYEE)
                .build();

        return new InMemoryUserDetailsManager(admin, employee);
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService(
            InMemoryUserDetailsManager inMemoryUserDetailsManager) {

        return new CompositeUserDetailsService(
                customUserDetailsService,
                inMemoryUserDetailsManager);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .securityMatcher(new NegatedRequestMatcher(
                        PathPatternRequestMatcher.withDefaults().matcher("/api/**")))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/h2-console/**",
                                "/login",
                                "/error"
                        ).permitAll()
                        .requestMatchers("/").authenticated()
                        .requestMatchers("/employees/**")
                        .hasRole(SecurityConstants.ADMIN)
                        .requestMatchers("/clients/**")
                        .hasAnyRole(
                                SecurityConstants.ADMIN,
                                SecurityConstants.EMPLOYEE)
                        .requestMatchers("/manufacturers/**", "/appliances/**")
                        .hasAnyRole(
                                SecurityConstants.ADMIN,
                                SecurityConstants.EMPLOYEE)
                        .requestMatchers("/orders/**")
                        .hasAnyRole(
                                SecurityConstants.ADMIN,
                                SecurityConstants.EMPLOYEE,
                                SecurityConstants.CLIENT)
                        .requestMatchers("/shop/**", "/cart/**")
                        .hasRole(SecurityConstants.CLIENT)
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .successHandler((request, response, authentication) -> {
                            log.info("Login successful: user={}, roles={}",
                                    authentication.getName(),
                                    authentication.getAuthorities());
                            response.sendRedirect("/");
                        })
                        .failureHandler((request, response, exception) -> {
                            log.warn("Login failed for user: {}",
                                    request.getParameter("username"));
                            response.sendRedirect("/login?error");
                        })
                        .permitAll())

                .logout(logout -> logout
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication != null) {
                                log.info("Logout: user={}", authentication.getName());
                            }
                            response.sendRedirect("/login?logout");
                        })
                        .permitAll())

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.warn("Access denied: user={}, uri={}",
                                    request.getUserPrincipal() != null
                                            ? request.getUserPrincipal().getName()
                                            : "anonymous",
                                    request.getRequestURI());
                            response.sendRedirect("/error?code=403");
                        }))

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**"))

                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
