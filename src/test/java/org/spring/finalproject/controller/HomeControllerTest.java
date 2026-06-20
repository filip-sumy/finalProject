package org.spring.finalproject.controller;

import org.junit.jupiter.api.Test;
import org.spring.finalproject.support.ControllerTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class HomeControllerTest extends ControllerTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageSource messageSource;

    @Test
    void home_returnsIndexView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void error_with403_setsForbiddenMessage() throws Exception {
        mockMvc.perform(get("/error").param("code", "403"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorCode", 403));
    }
}
