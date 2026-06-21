package org.spring.finalproject.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompositeUserDetailsServiceTest {

    @Mock
    private CustomUserDetailsService databaseUserDetailsService;

    @Mock
    private org.springframework.security.core.userdetails.UserDetailsService inMemoryUserDetailsService;

    private CompositeUserDetailsService compositeUserDetailsService;

    @BeforeEach
    void setUp() {
        compositeUserDetailsService = new CompositeUserDetailsService(
                databaseUserDetailsService,
                inMemoryUserDetailsService);
    }

    @Test
    void loadUserByUsername_returnsDatabaseUserWhenPresent() {
        UserDetails dbUser = User.builder()
                .username("john@test.com")
                .password("hash")
                .roles("CLIENT")
                .build();

        when(databaseUserDetailsService.loadUserByUsername("john@test.com"))
                .thenReturn(dbUser);

        UserDetails result = compositeUserDetailsService.loadUserByUsername("john@test.com");

        assertEquals("john@test.com", result.getUsername());
    }

    @Test
    void loadUserByUsername_fallsBackToInMemoryWhenNotInDatabase() {
        UserDetails inMemoryUser = User.builder()
                .username("employee")
                .password("hash")
                .roles("EMPLOYEE")
                .build();

        when(databaseUserDetailsService.loadUserByUsername("employee"))
                .thenThrow(new UsernameNotFoundException("not found"));
        when(inMemoryUserDetailsService.loadUserByUsername("employee"))
                .thenReturn(inMemoryUser);

        UserDetails result = compositeUserDetailsService.loadUserByUsername("employee");

        assertEquals("employee", result.getUsername());
    }

    @Test
    void loadUserByUsername_throwsWhenMissingEverywhere() {
        when(databaseUserDetailsService.loadUserByUsername("unknown"))
                .thenThrow(new UsernameNotFoundException("not found"));
        when(inMemoryUserDetailsService.loadUserByUsername("unknown"))
                .thenThrow(new UsernameNotFoundException("not found"));

        assertThrows(
                UsernameNotFoundException.class,
                () -> compositeUserDetailsService.loadUserByUsername("unknown"));
    }
}
