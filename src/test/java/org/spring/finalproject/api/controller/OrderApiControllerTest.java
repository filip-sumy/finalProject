package org.spring.finalproject.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.security.OrderSecurity;
import org.spring.finalproject.service.ClientService;
import org.spring.finalproject.service.OrderService;
import org.spring.finalproject.support.ControllerTestBase;
import org.spring.finalproject.support.MethodSecurityTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.spring.finalproject.api.exception.ApiExceptionHandler;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderApiController.class)
@Import({MethodSecurityTestConfig.class, ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class OrderApiControllerTest extends ControllerTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean(name = "orderSecurity")
    private OrderSecurity orderSecurity;

    @BeforeEach
    void setUp() {
        when(orderSecurity.isOwner(anyLong(), any())).thenReturn(false);
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void findById_deniedWhenNotOwner() throws Exception {
        when(orderSecurity.isOwner(eq(99L), any())).thenReturn(false);

        mockMvc.perform(get("/api/v1/orders/99"))
                .andExpect(status().isForbidden());

        verify(orderService, never()).findById(anyLong());
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void findById_allowedWhenOwner() throws Exception {
        when(orderSecurity.isOwner(eq(5L), any())).thenReturn(true);
        when(orderService.findById(5L)).thenReturn(new OrderDto());

        mockMvc.perform(get("/api/v1/orders/5"))
                .andExpect(status().isOk());

        verify(orderService).findById(5L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById_allowedForAdmin() throws Exception {
        when(orderService.findById(1L)).thenReturn(new OrderDto());

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk());

        verify(orderService).findById(1L);
    }
}
