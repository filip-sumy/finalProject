package org.spring.finalproject.controller;

import org.junit.jupiter.api.Test;
import org.spring.finalproject.dto.ClientDto;
import org.spring.finalproject.service.ClientService;
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

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest extends ControllerTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Test
    void findAll_returnsListView() throws Exception {
        when(clientService.findAll(any(), any()))
                .thenReturn(new PageImpl<>(List.of(new ClientDto())));

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/list"));
    }

    @Test
    void createForm_returnsForm() throws Exception {
        mockMvc.perform(get("/clients/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/form"));
    }

    @Test
    void editForm_returnsForm() throws Exception {
        when(clientService.findById(1L)).thenReturn(new ClientDto());

        mockMvc.perform(get("/clients/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/form"));
    }

    @Test
    void delete_redirectsToList() throws Exception {
        mockMvc.perform(post("/clients/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        verify(clientService).delete(1L);
    }
}
