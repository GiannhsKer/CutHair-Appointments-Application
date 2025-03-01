package com.barbershop.CutHair.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    public void addNewAppointment(Appointment appointment){
        Optional<Appointment> appointmentById = appointmentRepository.findById(appointment.getId());
        if(appointmentById.isPresent()){
            throw new IllegalStateException("Id taken");
        }
        appointmentRepository.save(appointment);
    }

    public void deleteAppointment(String id){
        boolean exists = appointmentRepository.existsById(id);
        if(!exists){
            throw new IllegalStateException("appointment with id " + id + " does not exist");
        }
        appointmentRepository.deleteById(id);
    }
}
