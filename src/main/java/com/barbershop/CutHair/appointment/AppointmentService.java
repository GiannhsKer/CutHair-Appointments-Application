package com.barbershop.CutHair.appointment;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAppointments(String id) {
        Optional<Appointment> appointmentById = appointmentRepository.findById(id);
        return appointmentById.map(List::of).orElseGet(appointmentRepository::findAll);
    }

    public void addNewAppointment(Appointment appointment) {
        Optional<Appointment> appointmentById = appointmentRepository.findById(appointment.getId());
        if (appointmentById.isPresent()) {
            throw new IllegalStateException("Id is taken");
        }
        if (appointment.getName().isBlank()) {
            throw new InvalidParameterException("Name field is invalid");
        } else if (appointment.getPhoneNumber().isBlank() || appointment.getPhoneNumber().length() != 10) {
            throw new InvalidParameterException("Phone number is invalid");
        } else if (appointment.getDateTime() == null || appointment.getDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidParameterException("Date is invalid");
        } else {
            appointmentRepository.save(appointment);
        }
    }

    public void deleteAppointment(String id) {
        boolean exists = appointmentRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("appointment with id " + id + " does not exist");
        }
        appointmentRepository.deleteById(id);
    }

    @Transactional
    public void editAppointment(String id, Appointment appointmentUpdated) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("appointment with id " + id + " does not exist"));
        if (appointmentUpdated.getName().isBlank()) {
            throw new InvalidParameterException("Name field is invalid");
        } else if (appointmentUpdated.getPhoneNumber().isBlank() || appointmentUpdated.getPhoneNumber().length() != 10
                || !appointmentUpdated.getPhoneNumber().startsWith("69")) {
            throw new InvalidParameterException("Phone number is invalid");
        } else if (appointmentUpdated.getDateTime() == null || appointmentUpdated.getDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidParameterException("Date is invalid");
        } else {
            appointment.setName(appointmentUpdated.getName());
            appointment.setPhoneNumber(appointmentUpdated.getPhoneNumber());
            appointment.setDateTime(appointmentUpdated.getDateTime());
        }
    }
}
