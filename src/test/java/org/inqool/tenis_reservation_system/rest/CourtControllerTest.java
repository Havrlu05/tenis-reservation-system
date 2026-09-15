package org.inqool.tenis_reservation_system.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.inqool.tenis_reservation_system.entities.Courts;
import org.inqool.tenis_reservation_system.rest.dto.CourtCreateDto;
import org.inqool.tenis_reservation_system.services.CourtsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(CourtController.class)
class CourtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourtsService courtsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldGetAllCourts() throws Exception {
        Courts court = new Courts();
        court.setId(1L);
        court.setName("Kurt 1");

        when(courtsService.findAll()).thenReturn(List.of(court));

        mockMvc.perform(get("/api/court/readAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Kurt 1"));
    }

    @Test
    void shouldGetCourtById() throws Exception {
        Courts court = new Courts();
        court.setId(1L);
        court.setName("Kurt 1");

        when(courtsService.readById(1L)).thenReturn(court);

        mockMvc.perform(get("/api/court/read/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Kurt 1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateCourtWhenAdmin() throws Exception {
        CourtCreateDto dto = new CourtCreateDto();
        dto.setName("Kurt 1");
        dto.setSurfaceId(1L);

        Courts savedCourt = new Courts();
        savedCourt.setId(1L);
        savedCourt.setName("Kurt 1");

        when(courtsService.create(any(CourtCreateDto.class))).thenReturn(savedCourt);

        mockMvc.perform(post("/api/court/createOne")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kurt 1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCourtWhenAdmin() throws Exception {
        CourtCreateDto dto = new CourtCreateDto();
        dto.setName("Upravený Kurt");
        dto.setSurfaceId(1L);

        Courts updatedCourt = new Courts();
        updatedCourt.setId(1L);
        updatedCourt.setName("Upravený Kurt");

        when(courtsService.update(eq(1L), any(CourtCreateDto.class))).thenReturn(updatedCourt);

        mockMvc.perform(put("/api/court/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Upravený Kurt"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCourtWhenAdmin() throws Exception {
        doNothing().when(courtsService).delete(1L);

        mockMvc.perform(delete("/api/court/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}