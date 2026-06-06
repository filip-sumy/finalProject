package org.spring.finalproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder encoder) {

        UserDetails admin =
                User.builder()
                        .username("admin")
                        .password(
                                encoder.encode("admin123"))
                        .roles("ADMIN")
                        .build();

        UserDetails employee =
                User.builder()
                        .username("employee")
                        .password(
                                encoder.encode("employee123"))
                        .roles("EMPLOYEE")
                        .build();

        UserDetails client =
                User.builder()
                        .username("client")
                        .password(
                                encoder.encode("client123"))
                        .roles("CLIENT")
                        .build();

        return new InMemoryUserDetailsManager(
                admin,
                employee,
                client
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/h2-console/**"
                        ).permitAll()

                        .requestMatchers("/")
                        .permitAll()

                        .requestMatchers("/employees/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/manufacturers/**")
                        .hasAnyRole(
                                "ADMIN",
                                "EMPLOYEE"
                        )

                        .requestMatchers("/appliances/**")
                        .hasAnyRole(
                                "ADMIN",
                                "EMPLOYEE"
                        )

                        .requestMatchers("/orders/**")
                        .hasAnyRole(
                                "ADMIN",
                                "EMPLOYEE",
                                "CLIENT"
                        )

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form.permitAll())

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        http.headers(headers ->
                headers.frameOptions(
                        frame -> frame.disable()));

        return http.build();
    }
}
