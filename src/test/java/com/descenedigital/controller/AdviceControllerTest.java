package com.descenedigital.controller;

import com.descenedigital.model.Advice;
import com.descenedigital.service.AdviceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdviceController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.descenedigital.security.JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = true)
@Import(com.descenedigital.config.TestSecurityConfig.class)
class AdviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdviceService adviceService;

    @Test
    void list_returnsOk() throws Exception {
        Mockito.when(adviceService.list(any())).thenReturn(new PageImpl<>(List.of(new Advice(1L, "a"))));
        mockMvc.perform(get("/api/advice")).andExpect(status().isOk());
    }

    @Test
    void get_returnsNotFound() throws Exception {
        Mockito.when(adviceService.getById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/advice/99")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void create_adminCanCreate() throws Exception {
        Advice created = new Advice(10L, "hi");
        Mockito.when(adviceService.create(any(Advice.class))).thenReturn(created);
        mockMvc.perform(post("/api/advice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"hi\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void create_userForbidden() throws Exception {
        mockMvc.perform(post("/api/advice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"hi\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void update_adminOkOrNotFound() throws Exception {
        Mockito.when(adviceService.update(eq(1L), any(Advice.class))).thenReturn(Optional.of(new Advice(1L, "x")));
        mockMvc.perform(put("/api/advice/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"changed\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void delete_adminNoContent() throws Exception {
        mockMvc.perform(delete("/api/advice/1"))
                .andExpect(status().isNoContent());
    }
}
