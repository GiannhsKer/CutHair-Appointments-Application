package com.gi.cuthair.appointment.controller;

import com.gi.cuthair.appointment.exception.AppointmentException;
import com.gi.cuthair.appointment.model.ApiResponse;
import com.gi.cuthair.appointment.model.Appointment;
import com.gi.cuthair.appointment.model.AppointmentRequest;
import com.gi.cuthair.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appointments")
@Slf4j
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Appointment>>> getAllAppointments() {
        log.info("GET /api/v1/appointments - Fetching all appointments");
        List<Appointment> appointments = new ArrayList<>(appointmentService.getAllAppointments());
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved successfully", appointments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Appointment>> getAppointmentById(@PathVariable UUID id) {
        log.info("GET /api/v1/appointments/{} - Fetching appointment by id", id);
        return appointmentService.getAppointmentById(id)
                .map(appointment -> ResponseEntity.ok(ApiResponse.success("Appointment retrieved successfully", appointment)))
                .orElseGet(() ->ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Appointment not found with id: " + id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Appointment>>> searchAppointments(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("GET /api/v1/appointments/search - Searching appointments with name: {}, phone: {}, date: {}", name, phone, date);

        List<Appointment> appointments;

        if (name != null) {
            appointments = new ArrayList<>(appointmentService.getAppointmentByName(name).stream().toList());
        } else if (phone != null) {
            appointments = new ArrayList<>(appointmentService.getAppointmentByPhone(phone).stream().toList());
        } else if (date != null) {
            appointments = new ArrayList<>(appointmentService.getAppointmentsByDate(date));
        } else if (startDate != null && endDate != null) {
            appointments = new ArrayList<>(appointmentService.getAppointmentsByDateRange(startDate, endDate));
        } else {
            appointments = new ArrayList<>(appointmentService.getAllAppointments());
        }

        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved successfully", appointments));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Appointment>> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        log.info("POST /api/v1/appointments - Creating new appointment for: {}", request.getName());
        try {
            Appointment createdAppointment = appointmentService.createAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.success("Appointment created successfully", createdAppointment));
        } catch (AppointmentException e) {
            log.error("Error creating appointment: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Appointment>> updateAppointment(
            @Valid @RequestBody AppointmentRequest request, @PathVariable UUID id) {
        log.info("PATCH /api/v1/appointments/{} - Updating appointment", id);
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(request, id);
            return ResponseEntity.ok(ApiResponse.success("Appointment updated successfully", updatedAppointment));
        } catch (AppointmentException e) {
            log.error("Error updating appointment: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAppointment(@PathVariable UUID id) {
        log.info("DELETE /api/v1/appointments/{} - Deleting appointment", id);
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok(ApiResponse.success("Appointment deleted successfully", null));
        } catch (AppointmentException e) {
            log.error("Error deleting appointment: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(errorMessage));
    }

    @ExceptionHandler(AppointmentException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppointmentException(AppointmentException e) {
        log.error("AppointmentException: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred"));
    }
}
