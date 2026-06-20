package org.spring.finalproject.controller;

import org.junit.jupiter.api.Test;
import org.spring.finalproject.dto.EmployeeDto;
import org.spring.finalproject.service.EmployeeService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(EmployeeController.class)
@Import(MethodSecurityTestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest extends ControllerTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_returnsListView() throws Exception {
        when(employeeService.findAll(any(), any()))
                .thenReturn(new PageImpl<>(List.of(new EmployeeDto())));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/list"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createForm_returnsForm() throws Exception {
        mockMvc.perform(get("/employees/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/form"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editForm_returnsForm() throws Exception {
        when(employeeService.findById(1L)).thenReturn(new EmployeeDto());

        mockMvc.perform(get("/employees/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee/form"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_redirectsToList() throws Exception {
        mockMvc.perform(post("/employees/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));

        verify(employeeService).delete(1L);
    }
}
