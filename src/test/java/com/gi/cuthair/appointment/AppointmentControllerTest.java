//package com.gi.CutHair.appointment;
//
//import com.gi.CutHair.appointment.dto.AppointmentRequest;
//import com.gi.CutHair.appointment.dto.AppointmentResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AppointmentController.class)
//class AppointmentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AppointmentService appointmentService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void getAllAppointments_ShouldReturnAppointments() throws Exception {
//        // Given
//        Appointment appointment = new Appointment();
//        appointment.setId("test-id");
//        appointment.setName("John Doe");
//        appointment.setPhoneNumber("1234567890");
//        appointment.setDateTime(LocalDateTime.now().plusDays(1));
//
//        when(appointmentService.getAllAppointments()).thenReturn(Arrays.asList(appointment));
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/appointments"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data[0].id").value("test-id"))
//                .andExpect(jsonPath("$.data[0].name").value("John Doe"));
//    }
//
//    @Test
//    void getAppointmentById_WhenExists_ShouldReturnAppointment() throws Exception {
//        // Given
//        Appointment appointment = new Appointment();
//        appointment.setId("test-id");
//        appointment.setName("John Doe");
//        appointment.setPhoneNumber("1234567890");
//        appointment.setDateTime(LocalDateTime.now().plusDays(1));
//
//        when(appointmentService.getAppointmentById("test-id")).thenReturn(Optional.of(appointment));
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/appointments/test-id"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value("test-id"))
//                .andExpect(jsonPath("$.data.name").value("John Doe"));
//    }
//
//    @Test
//    void getAppointmentById_WhenNotExists_ShouldReturn404() throws Exception {
//        // Given
//        when(appointmentService.getAppointmentById("non-existent")).thenReturn(Optional.empty());
//
//        // When & Then
//        mockMvc.perform(get("/api/v1/appointments/non-existent"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.success").value(false));
//    }
//
//    @Test
//    void createAppointment_WithValidRequest_ShouldReturn201() throws Exception {
//        // Given
//        AppointmentRequest request = new AppointmentRequest();
//        request.setName("John Doe");
//        request.setPhoneNumber("1234567890");
//        request.setDateTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(30));
//
//        Appointment savedAppointment = new Appointment();
//        savedAppointment.setId("new-id");
//        savedAppointment.setName(request.getName());
//        savedAppointment.setPhoneNumber(request.getPhoneNumber());
//        savedAppointment.setDateTime(request.getDateTime());
//
//        when(appointmentService.createAppointment(any(AppointmentRequest.class))).thenReturn(savedAppointment);
//
//        // When & Then
//        mockMvc.perform(post("/api/v1/appointments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value("new-id"));
//    }
//
//    @Test
//    void createAppointment_WithInvalidRequest_ShouldReturn400() throws Exception {
//        // Given
//        AppointmentRequest request = new AppointmentRequest();
//        request.setName(""); // Invalid: empty name
//        request.setPhoneNumber("1234567890");
//        request.setDateTime(LocalDateTime.now().plusDays(1));
//
//        // When & Then
//        mockMvc.perform(post("/api/v1/appointments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//}