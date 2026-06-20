package org.spring.finalproject.controller;

import org.junit.jupiter.api.Test;
import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.service.ApplianceService;
import org.spring.finalproject.support.ControllerTestBase;
import org.spring.finalproject.support.MethodSecurityTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ShopController.class)
@Import(MethodSecurityTestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class ShopControllerTest extends ControllerTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplianceService applianceService;

    @Test
    @WithMockUser(roles = "CLIENT")
    void catalog_returnsShopView() throws Exception {
        ApplianceDto appliance = new ApplianceDto();
        appliance.setId(1L);
        appliance.setName("TV");
        appliance.setPrice(BigDecimal.TEN);
        appliance.setQuantity(5);

        when(applianceService.findAll()).thenReturn(List.of(appliance));

        mockMvc.perform(get("/shop"))
                .andExpect(status().isOk())
                .andExpect(view().name("shop/catalog"))
                .andExpect(model().attributeExists("appliances"));
    }
}
