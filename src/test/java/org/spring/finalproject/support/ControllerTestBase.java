package org.spring.finalproject.support;

import org.spring.finalproject.exception.ExceptionMessageResolver;
import org.spring.finalproject.service.cart.Cart;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Provides beans required by {@link org.spring.finalproject.config.CartAdvice}
 * and {@link org.spring.finalproject.controller.CartController}
 * in {@code @WebMvcTest} slices.
 */
public abstract class ControllerTestBase {

    @MockitoBean
    protected Cart cart;

    @MockitoBean
    protected ExceptionMessageResolver messageResolver;
}
