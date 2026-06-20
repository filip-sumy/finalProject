package org.spring.finalproject.config;

import lombok.RequiredArgsConstructor;
import org.spring.finalproject.service.cart.Cart;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class CartAdvice {

    private final Cart cart;

    @ModelAttribute("cartItemCount")
    public int cartItemCount(Authentication authentication) {
        if (authentication == null) {
            return 0;
        }

        boolean isClient = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"));

        return isClient ? cart.getItemCount() : 0;
    }
}
