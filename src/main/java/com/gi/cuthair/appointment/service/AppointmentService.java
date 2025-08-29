package com.gi.cuthair.appointment.service;

import com.gi.cuthair.appointment.model.Appointment;
import com.gi.cuthair.appointment.model.AppointmentRequest;
import com.gi.cuthair.appointment.exception.AppointmentException;
import com.gi.cuthair.appointment.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private static final String APPOINTMENT_INVALID = "Please place your appointments Thursday - Friday 10:00 - 20:30, Saturday 10:00 - 15:00";
    private static final String APPOINTMENT_CONFLICT = "There is already an appointment scheduled within 30 minutes of the requested time";

//    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {
        log.info("Fetching all appointments");
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        log.info("Fetching appointment with id: {}", id);
        return appointmentRepository.findById(id);
    }

    public Optional<Appointment> getAppointmentByName(String name) {
        log.info("Fetching appointment with name: {}", name);
        return appointmentRepository.findByName(name);
    }

    public Optional<Appointment> getAppointmentByPhone(String phoneNumber) {
        log.info("Fetching appointment with phone: {}", phoneNumber);
        return appointmentRepository.findByPhone(phoneNumber);
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        log.info("Fetching appointments for date: {}", date);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByDate(startOfDay, endOfDay);
    }

    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Fetching appointments between {} and {}", start, end);
        return appointmentRepository.findByDateRange(start, end, null);
    }

    @Transactional
    public Appointment createAppointment(AppointmentRequest request) {
        log.info("Creating new appointment for: {}", request.getName());

        // Check for conflicts
        checkForConflictAppointments(request.getDateTime(), null);

        Appointment appointment = new Appointment(request.getName(),
                request.getPhoneNumber(),
                request.getDateTime()
        );

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment created successfully with id: {}", savedAppointment.getId());
        return savedAppointment;
    }

    @Transactional
    public Appointment updateAppointment(AppointmentRequest request, UUID id) {
        log.info("Updating appointment with id: {}", id);

        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentException("Appointment with id " + id + " does not exist"));

        // Check for conflicts (excluding current appointment)
        checkForConflictAppointments(request.getDateTime(), id);

        existingAppointment.setName(request.getName());
        existingAppointment.setPhoneNumber(request.getPhoneNumber());
        existingAppointment.setDateTime(request.getDateTime());

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        log.info("Appointment updated successfully with id: {}", updatedAppointment.getId());
        return updatedAppointment;
    }

    @Transactional
    public void deleteAppointment(UUID id) {
        log.info("Deleting appointment with id: {}", id);

        if (!appointmentRepository.existsById(id)) {
            throw new AppointmentException("Appointment with id " + id + " does not exist");
        }

        appointmentRepository.deleteById(id);
        log.info("Appointment deleted successfully with id: {}", id);
    }

    private void checkForConflictAppointments(LocalDateTime dateTime, UUID id) {

        // Check if appointment is within business hours and days
        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        LocalTime appointmentTime = dateTime.toLocalTime();
        if(dayOfWeek == 1 || dayOfWeek == 7
                || appointmentTime.isBefore(LocalTime.of(10, 0))
                || appointmentTime.isAfter(LocalTime.of(20, 30))){
            throw new AppointmentException(APPOINTMENT_INVALID);
        } else if (dayOfWeek == 6 && appointmentTime.isBefore(LocalTime.of(10, 0))
                || appointmentTime.isAfter(LocalTime.of(15, 0))){
            throw new AppointmentException(APPOINTMENT_INVALID);
        }

        // Check if there's already an appointment within 30 minutes of the requested time
        List<Appointment> conflictingAppointments = appointmentRepository.findByDateRange(
                dateTime.minusMinutes(30), dateTime.plusMinutes(30),id);

        if (!conflictingAppointments.isEmpty()) {
            throw new AppointmentException(APPOINTMENT_CONFLICT);
        }
    }

}
