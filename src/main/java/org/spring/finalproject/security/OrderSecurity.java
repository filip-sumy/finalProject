package org.spring.finalproject.security;

import lombok.RequiredArgsConstructor;
import org.spring.finalproject.repository.OrderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("orderSecurity")
@RequiredArgsConstructor
public class OrderSecurity {

    private final OrderRepository orderRepository;

    public boolean isOwner(Long orderId, Object authentication) {
        if (!(authentication instanceof Authentication auth)) {
            return false;
        }

        if (!auth.isAuthenticated()) {
            return false;
        }

        String email = auth.getName();

        return orderRepository.existsByIdAndClient_Email(orderId, email);
    }
}