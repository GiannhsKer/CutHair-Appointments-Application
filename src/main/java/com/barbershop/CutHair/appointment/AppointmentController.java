package com.barbershop.CutHair.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<Appointment> getAppointment(){
        return appointmentService.getAppointments();
    }

    @PostMapping
    public void registerNewAppointment(@RequestBody Appointment appointment){
        appointmentService.addNewAppointment(appointment);
    }

    @DeleteMapping(path = "{id}")
    public void deleteAppointment(@PathVariable("id") String id){
        appointmentService.deleteAppointment(id);
    }
}
