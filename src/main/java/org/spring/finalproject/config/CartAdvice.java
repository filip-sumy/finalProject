package org.spring.finalproject.config;

import lombok.RequiredArgsConstructor;
import org.spring.finalproject.service.cart.Cart;
import org.spring.finalproject.security.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class CartAdvice {

    private final Cart cart;

    @ModelAttribute("cartItemCount")
    public int cartItemCount(Authentication authentication) {
        return SecurityUtils.isClient(authentication) ? cart.getItemCount() : 0;
    }
}
