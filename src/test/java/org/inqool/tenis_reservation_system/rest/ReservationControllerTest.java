package org.inqool.tenis_reservation_system.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.inqool.tenis_reservation_system.entities.Reservations;
import org.inqool.tenis_reservation_system.rest.dto.ReservationCreateDto;
import org.inqool.tenis_reservation_system.rest.dto.ReservationResponseDto;
import org.inqool.tenis_reservation_system.services.JwtService;
import org.inqool.tenis_reservation_system.services.ReservationsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationsService reservationsService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @WithMockUser(roles = "USER")
    void shouldCreateReservation() throws Exception {
        ReservationCreateDto dto = new ReservationCreateDto();
        dto.setCourtId(1L);
        dto.setCustomersName("Lukáš Havránek");
        dto.setCustomersPhone("123456789");
        dto.setStart_time(LocalDateTime.of(2026, 9, 15, 14, 00));
        dto.setEnd_time(LocalDateTime.of(2026, 9, 15, 16, 00));
        dto.setIs_doubles(true);


        ReservationResponseDto responseDto = new ReservationResponseDto();

        when(reservationsService.create(any(ReservationCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/reservation/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetReservationById() throws Exception {
        Reservations reservation = new Reservations();
        reservation.setId(1L);

        when(reservationsService.read(1L)).thenReturn(reservation);

        mockMvc.perform(get("/api/reservation/read/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteReservation() throws Exception {
        doNothing().when(reservationsService).delete(1L);

        mockMvc.perform(delete("/api/reservation/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetReservationsByCourt() throws Exception {
        Reservations reservation = new Reservations();
        reservation.setId(1L);

        when(reservationsService.findByCourtId(1L)).thenReturn(List.of(reservation));

        mockMvc.perform(get("/api/reservation/court/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldGetReservationsByPhone() throws Exception {
        Reservations reservation = new Reservations();
        reservation.setId(1L);

        when(reservationsService.getReservationsByPhone(eq("123456789"), eq(false)))
                .thenReturn(List.of(reservation));

        mockMvc.perform(get("/api/reservation/phone/123456789")
                        .param("onlyFuture", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}