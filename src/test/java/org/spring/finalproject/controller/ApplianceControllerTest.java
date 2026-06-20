package org.spring.finalproject.controller;

import org.junit.jupiter.api.Test;
import org.spring.finalproject.dto.ApplianceDto;
import org.spring.finalproject.dto.ManufacturerDto;
import org.spring.finalproject.service.ApplianceService;
import org.spring.finalproject.service.ManufacturerService;
import org.spring.finalproject.support.ControllerTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ApplianceController.class)
@AutoConfigureMockMvc(addFilters = false)
class ApplianceControllerTest extends ControllerTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplianceService applianceService;

    @MockitoBean
    private ManufacturerService manufacturerService;

    @Test
    void findAll_returnsListView() throws Exception {
        when(applianceService.findAll(any(), any()))
                .thenReturn(new PageImpl<>(List.of(new ApplianceDto())));

        mockMvc.perform(get("/appliances"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliance/list"));
    }

    @Test
    void createForm_returnsForm() throws Exception {
        when(manufacturerService.findAll()).thenReturn(List.of(new ManufacturerDto()));

        mockMvc.perform(get("/appliances/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliance/form"));
    }

    @Test
    void editForm_returnsForm() throws Exception {
        when(applianceService.findById(1L)).thenReturn(new ApplianceDto());
        when(manufacturerService.findAll()).thenReturn(List.of(new ManufacturerDto()));

        mockMvc.perform(get("/appliances/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("appliance/form"));
    }

    @Test
    void delete_redirectsToList() throws Exception {
        mockMvc.perform(post("/appliances/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appliances"));

        verify(applianceService).delete(1L);
    }
}
