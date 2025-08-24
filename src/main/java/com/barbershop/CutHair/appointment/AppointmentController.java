package com.barbershop.CutHair.appointment;

import com.barbershop.CutHair.appointment.dto.ApiResponse;
import com.barbershop.CutHair.appointment.dto.AppointmentRequest;
import com.barbershop.CutHair.appointment.dto.AppointmentResponse;
import com.barbershop.CutHair.appointment.exception.AppointmentException;
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
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {
        log.info("GET /api/v1/appointments - Fetching all appointments");
        List<AppointmentResponse> appointments = appointmentService.getAllAppointments()
                .stream()
                .map(AppointmentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved successfully", appointments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable UUID id) {
        log.info("GET /api/v1/appointments/{} - Fetching appointment by id", id);
        return appointmentService.getAppointmentById(id)
                .map(appointment -> ResponseEntity.ok(ApiResponse.success("Appointment retrieved successfully", AppointmentResponse.from(appointment))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Appointment not found with id: " + id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> searchAppointments(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("GET /api/v1/appointments/search - Searching appointments with name: {}, phone: {}, date: {}", name, phone, date);

        List<AppointmentResponse> appointments;

        if (name != null) {
            appointments = appointmentService.getAppointmentByName(name)
                    .map(appointment -> List.of(AppointmentResponse.from(appointment)))
                    .orElse(List.of());
        } else if (phone != null) {
            appointments = appointmentService.getAppointmentByPhone(phone)
                    .map(appointment -> List.of(AppointmentResponse.from(appointment)))
                    .orElse(List.of());
        } else if (date != null) {
            appointments = appointmentService.getAppointmentsByDate(date)
                    .stream()
                    .map(AppointmentResponse::from)
                    .collect(Collectors.toList());
        } else if (startDate != null && endDate != null) {
            appointments = appointmentService.getAppointmentsByDateRange(startDate, endDate)
                    .stream()
                    .map(AppointmentResponse::from)
                    .collect(Collectors.toList());
        } else {
            appointments = appointmentService.getAllAppointments()
                    .stream()
                    .map(AppointmentResponse::from)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved successfully", appointments));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        log.info("POST /api/v1/appointments - Creating new appointment for: {}", request.getName());
        try {
            Appointment createdAppointment = appointmentService.createAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Appointment created successfully", AppointmentResponse.from(createdAppointment)));
        } catch (AppointmentException e) {
            log.error("Error creating appointment: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointment(
            @Valid @RequestBody AppointmentRequest request, @PathVariable UUID id) {
        log.info("PATCH /api/v1/appointments/{} - Updating appointment", id);
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(request, id);
            return ResponseEntity.ok(ApiResponse.success("Appointment updated successfully", AppointmentResponse.from(updatedAppointment)));
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
