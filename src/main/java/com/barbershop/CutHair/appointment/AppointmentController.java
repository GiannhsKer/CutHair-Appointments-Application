package com.barbershop.CutHair.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    
    @Autowired
    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<Appointment> getAppointments(
            @RequestHeader(value = "id", required = false) String id,
            @RequestHeader(value = "name", required = false) String name,
            @RequestHeader(value = "dateTime", required = false) LocalDateTime dateTime,
            @RequestHeader(value = "phoneNumber", required = false) String phoneNumber){
        if(id != null){
            return appointmentService.getAppointments(AppointmentProps.ID, id);
        }
        else if(name != null){
            return appointmentService.getAppointments(AppointmentProps.NAME, name);
        }
        else if(phoneNumber != null){
            return appointmentService.getAppointments(AppointmentProps.PHONE, phoneNumber);
        }
        else if(dateTime != null){
            return appointmentService.getAppointments(AppointmentProps.DATE, dateTime);
        }
        return appointmentService.getAppointments(AppointmentProps.NULL,null);
    }

    @PostMapping
    public void registerNewAppointment(@RequestBody Appointment appointment){
        if(null == appointment.getId()){
            appointment.setId(UUID.randomUUID().toString());
        }
        appointmentService.addNewAppointment(appointment);
    }

    @DeleteMapping
    public void deleteAppointment(@RequestHeader("X-Client-ID") String id){
        appointmentService.deleteAppointment(id);
    }

    @PutMapping
    public void editAppointment(@RequestHeader("X-Client-ID") String id, @RequestBody Appointment appointment){
        appointmentService.editAppointment(id, appointment);
    }
}
