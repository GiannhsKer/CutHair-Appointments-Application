package com.barbershop.CutHair.appointment;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public <T> List<Appointment> getAppointments(AppointmentProps propertyEnum, T property) {

        return switch (propertyEnum) {
            case AppointmentProps.ID ->
                    appointmentRepository.findById((String) property).map(List::of).orElseGet(Collections::emptyList);
            case AppointmentProps.NAME ->
                    appointmentRepository.findByName((String) property).map(List::of).orElseGet(Collections::emptyList);
            case AppointmentProps.PHONE ->
                    appointmentRepository.findByPhone((String) property).map(List::of).orElseGet(Collections::emptyList);
            case AppointmentProps.DATE -> {
                LocalDateTime dateTime = (LocalDateTime) property;
                LocalDate date = dateTime.toLocalDate();
                yield appointmentRepository.findByDate(date.atStartOfDay(), date.atTime(LocalTime.MAX));
            }
            default -> appointmentRepository.findAll();
        };
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
        if (appointmentUpdated == null) {
            throw new IllegalArgumentException("Form is empty, nothing to update");
        } else {
            if (null != appointmentUpdated.getName()) {
                if(appointmentUpdated.getName().isBlank()){
                    throw new InvalidParameterException("Name is invalid");
                }
                else {
                    appointment.setName(appointmentUpdated.getName());
                }
            }
            if (null != appointmentUpdated.getPhoneNumber()) {
                if (appointmentUpdated.getPhoneNumber().isBlank() || appointmentUpdated.getPhoneNumber().length() != 10
                        || !appointmentUpdated.getPhoneNumber().startsWith("69")) {
                    throw new InvalidParameterException("Phone number is invalid");
                } else {
                    appointment.setPhoneNumber(appointmentUpdated.getPhoneNumber());
                }
            }
            if (appointmentUpdated.getDateTime() != null) {
                if (appointmentUpdated.getDateTime().isBefore(LocalDateTime.now())) {
                    throw new InvalidParameterException("Date is invalid");
                } else {
                    appointment.setDateTime(appointmentUpdated.getDateTime());
                }
            }
        }
    }
}
