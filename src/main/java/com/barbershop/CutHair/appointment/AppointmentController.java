package com.barbershop.CutHair.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "dateTime", required = false) LocalDateTime dateTime,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber){
//        if(!id.isBlank()){
//            return appointmentService.getAppointments(id);
//        }
//        else if(!name.isBlank()){
//            return appointmentService.getAppointments(name);
//        }
//        else if(!phoneNumber.isBlank()){
//            return appointmentService.getAppointments(phoneNumber);
//        }
//        else if(dateTime != null){
//            return appointmentService.getAppointments(dateTime);
//        }
        return appointmentService.getAppointments(id);
    }

    @PostMapping
    public void registerNewAppointment(@RequestBody Appointment appointment){
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
