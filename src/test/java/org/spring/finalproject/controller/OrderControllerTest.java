package org.spring.finalproject.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.dto.OrderDto;
import org.spring.finalproject.dto.OrderRowDto;
import org.spring.finalproject.dto.response.ClientResponseDto;
import org.spring.finalproject.entity.OrderStatus;
import org.spring.finalproject.security.OrderSecurity;
import org.spring.finalproject.service.ApplianceService;
import org.spring.finalproject.service.ClientService;
import org.spring.finalproject.service.OrderService;
import org.spring.finalproject.support.ControllerTestBase;
import org.spring.finalproject.support.MethodSecurityTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(OrderController.class)
@Import(MethodSecurityTestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest extends ControllerTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean
    private ApplianceService applianceService;

    @MockitoBean(name = "orderSecurity")
    private OrderSecurity orderSecurity;

    @BeforeEach
    void setUp() {
        when(orderSecurity.isOwner(anyLong(), any())).thenReturn(true);
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void findAll_asAdmin_returnsOrderList() throws Exception {
        when(orderService.findAll(any(), any()))
                .thenReturn(new PageImpl<>(List.of(sampleOrder())));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/list"));
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void findAll_asClient_usesClientScope() throws Exception {
        ClientDto client = new ClientDto();
        client.setId(1L);

        when(clientService.findByEmail("john@test.com")).thenReturn(client);
        when(orderService.findByClient(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(sampleOrder())));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/list"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createForm_returnsForm() throws Exception {
        when(applianceService.findAll()).thenReturn(List.of());
        when(clientService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/orders/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/form"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_validOrder_redirectsToList() throws Exception {
        when(applianceService.findAll()).thenReturn(List.of());
        when(clientService.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/orders/create")
                        .with(csrf())
                        .param("clientId", "1")
                        .param("rows[0].applianceId", "2")
                        .param("rows[0].quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService).create(any(OrderDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void approve_redirectsToOrders() throws Exception {
        mockMvc.perform(post("/orders/approve/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService).approve(1L);
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void editForm_asOwner_returnsForm() throws Exception {
        when(orderService.findById(5L)).thenReturn(sampleOrder());
        when(applianceService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/orders/edit/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/form"));
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "CLIENT")
    void update_asClient_redirectsToOrders() throws Exception {
        ClientDto client = new ClientDto();
        client.setId(1L);

        when(clientService.findByEmail("john@test.com")).thenReturn(client);

        mockMvc.perform(post("/orders/edit/5")
                        .with(csrf())
                        .param("rows[0].applianceId", "1")
                        .param("rows[0].quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService).update(eq(5L), any(OrderDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_missingClientId_returnsForm() throws Exception {
        when(applianceService.findAll()).thenReturn(List.of());
        when(clientService.findAll()).thenReturn(List.of());

        mockMvc.perform(post("/orders/create")
                        .with(csrf())
                        .param("rows[0].applianceId", "1")
                        .param("rows[0].quantity", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/form"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_redirectsToOrders() throws Exception {
        mockMvc.perform(post("/orders/delete/3").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService).delete(3L);
    }

    private static OrderDto sampleOrder() {
        OrderDto order = new OrderDto();
        order.setId(1L);
        order.setClientId(1L);
        order.setStatus(OrderStatus.CREATED);
        order.setRows(List.of(new OrderRowDto(1L, 1)));

        ClientResponseDto client = new ClientResponseDto();
        client.setFirstName("John");
        client.setLastName("Smith");
        order.setClient(client);

        return order;
    }
}
