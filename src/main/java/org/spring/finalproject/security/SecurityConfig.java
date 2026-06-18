package org.spring.finalproject.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

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

        UserDetails client = User.builder()
                .username("client")
                .password(encoder.encode("Client123!"))
                .roles(SecurityConstants.CLIENT)
                .build();

        UserDetailsService inMemory =
                new InMemoryUserDetailsManager(admin, employee, client);

        return username -> {
            try {
                return customUserDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException ex) {
                log.debug("Falling back to in-memory auth for: {}", username);
                return inMemory.loadUserByUsername(username);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
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
                        .hasRole(
                                SecurityConstants.ADMIN)
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
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll())

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/error?code=403"))

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**"))

                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
