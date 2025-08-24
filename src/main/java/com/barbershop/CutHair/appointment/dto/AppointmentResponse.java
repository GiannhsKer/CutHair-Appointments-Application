package com.barbershop.CutHair.appointment.dto;

import com.barbershop.CutHair.appointment.Appointment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentResponse {
    private UUID id;
    private String name;
    private String phoneNumber;
    private LocalDateTime dateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static AppointmentResponse from(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setName(appointment.getName());
        response.setPhoneNumber(appointment.getPhoneNumber());
        response.setDateTime(appointment.getDateTime());
        response.setCreatedAt(appointment.getCreatedAt());
        response.setUpdatedAt(appointment.getUpdatedAt());
        return response;
    }
} 