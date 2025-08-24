//package com.barbershop.CutHair.appointment;
//
//import com.barbershop.CutHair.appointment.dto.AppointmentRequest;
//import com.barbershop.CutHair.appointment.exception.AppointmentException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class AppointmentServiceTest {
//
//    @Mock
//    private AppointmentRepository appointmentRepository;
//
//    @InjectMocks
//    private AppointmentService appointmentService;
//
//    private AppointmentRequest validRequest;
//
//    @BeforeEach
//    void setUp() {
//        validRequest = new AppointmentRequest();
//        validRequest.setName("John Doe");
//        validRequest.setPhoneNumber("1234567890");
//        validRequest.setDateTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(30));
//    }
//
//    @Test
//    void createAppointment_WithValidRequest_ShouldSucceed() {
//        // Given
//        Appointment savedAppointment = new Appointment();
//        savedAppointment.setId("test-id");
//        savedAppointment.setName(validRequest.getName());
//        savedAppointment.setPhoneNumber(validRequest.getPhoneNumber());
//        savedAppointment.setDateTime(validRequest.getDateTime());
//
//        when(appointmentRepository.findByDateRange(any(), any())).thenReturn(java.util.Collections.emptyList());
//        when(appointmentRepository.save(any())).thenReturn(savedAppointment);
//
//        // When
//        Appointment result = appointmentService.createAppointment(validRequest);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(validRequest.getName(), result.getName());
//        assertEquals(validRequest.getPhoneNumber(), result.getPhoneNumber());
//        assertEquals(validRequest.getDateTime(), result.getDateTime());
//    }
//
//    @Test
//    void createAppointment_WithEmptyName_ShouldThrowException() {
//        // Given
//        validRequest.setName("");
//
//        // When & Then
//        assertThrows(AppointmentException.class, () -> appointmentService.createAppointment(validRequest));
//    }
//
//    @Test
//    void createAppointment_WithInvalidPhoneNumber_ShouldThrowException() {
//        // Given
//        validRequest.setPhoneNumber("123"); // Too short
//
//        // When & Then
//        assertThrows(AppointmentException.class, () -> appointmentService.createAppointment(validRequest));
//    }
//
//    @Test
//    void createAppointment_WithPastDateTime_ShouldThrowException() {
//        // Given
//        validRequest.setDateTime(LocalDateTime.now().minusDays(1));
//
//        // When & Then
//        assertThrows(AppointmentException.class, () -> appointmentService.createAppointment(validRequest));
//    }
//
//    @Test
//    void createAppointment_OutsideBusinessHours_ShouldThrowException() {
//        // Given
//        validRequest.setDateTime(LocalDateTime.now().plusDays(1).withHour(20).withMinute(0)); // 8 PM
//
//        // When & Then
//        assertThrows(AppointmentException.class, () -> appointmentService.createAppointment(validRequest));
//    }
//}