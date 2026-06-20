package org.spring.finalproject.controller;

import org.junit.jupiter.api.Test;
import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.service.ApplianceService;
import org.spring.finalproject.service.ClientService;
import org.spring.finalproject.service.OrderService;
import org.spring.finalproject.dto.OrderRowDto;
import org.spring.finalproject.support.ControllerTestBase;
import org.spring.finalproject.support.MethodSecurityTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CartController.class)
@Import(MethodSecurityTestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest extends ControllerTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplianceService applianceService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ClientService clientService;

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void view_returnsCartPage() throws Exception {
        when(cart.getItems()).thenReturn(java.util.List.of());
        when(cart.getTotalPrice()).thenReturn(BigDecimal.ZERO);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/view"));
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void add_redirectsToCart() throws Exception {
        ApplianceDto appliance = new ApplianceDto();
        appliance.setId(1L);
        appliance.setName("TV");
        appliance.setPrice(BigDecimal.TEN);
        appliance.setQuantity(10);

        when(applianceService.findById(1L)).thenReturn(appliance);

        mockMvc.perform(post("/cart/add")
                        .with(csrf())
                        .param("applianceId", "1")
                        .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cart).addItem(appliance, 2);
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void checkout_createsOrderAndClearsCart() throws Exception {
        ClientDto client = new ClientDto();
        client.setId(1L);

        when(cart.isEmpty()).thenReturn(false);
        when(clientService.findByEmail("john@test.com")).thenReturn(client);
        OrderDto orderDto = new OrderDto();
        orderDto.setRows(List.of(new OrderRowDto(1L, 1)));
        when(cart.toOrderDto()).thenReturn(orderDto);

        mockMvc.perform(post("/cart/checkout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService).create(any(OrderDto.class));
        verify(cart).clear();
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void checkout_emptyCart_redirectsToCart() throws Exception {
        when(cart.isEmpty()).thenReturn(true);

        mockMvc.perform(post("/cart/checkout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void update_redirectsToCart() throws Exception {
        ApplianceDto appliance = new ApplianceDto();
        appliance.setId(1L);
        appliance.setQuantity(10);

        when(applianceService.findById(1L)).thenReturn(appliance);

        mockMvc.perform(post("/cart/update/1")
                        .with(csrf())
                        .param("quantity", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cart).updateQuantity(1L, 3, appliance);
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void remove_redirectsToCart() throws Exception {
        mockMvc.perform(post("/cart/remove/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cart).removeItem(1L);
    }
}
