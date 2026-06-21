package org.spring.finalproject.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Database-backed authentication first; in-memory users ({@code admin}, {@code employee})
 * when the username is not found in the database.
 */
@Slf4j
public class CompositeUserDetailsService implements UserDetailsService {

    private final CustomUserDetailsService databaseUserDetailsService;
    private final UserDetailsService inMemoryUserDetailsService;

    public CompositeUserDetailsService(
            CustomUserDetailsService databaseUserDetailsService,
            UserDetailsService inMemoryUserDetailsService) {

        this.databaseUserDetailsService = databaseUserDetailsService;
        this.inMemoryUserDetailsService = inMemoryUserDetailsService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return databaseUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            log.debug("Database user not found, trying in-memory auth: {}", username);
            return inMemoryUserDetailsService.loadUserByUsername(username);
        }
    }
}
