package org.spring.finalproject.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean isClient(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        return hasClientRole(authentication.getAuthorities());
    }

    public static boolean isClient(UserDetails user) {
        if (user == null) {
            return false;
        }
        return hasClientRole(user.getAuthorities());
    }

    private static boolean hasClientRole(Iterable<? extends GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            if ("ROLE_CLIENT".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
