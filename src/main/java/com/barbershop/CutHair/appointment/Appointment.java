package com.barbershop.CutHair.appointment;

import java.time.LocalDate;
import java.util.UUID;

public class Appointment {

    private UUID id;
    private String name;
    private Long phoneNumber;
    private LocalDate date_time;

    public Appointment() {
    }

    public Appointment(UUID id, String name, Long phoneNumber, LocalDate date_time) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.date_time = date_time;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDate_time() {
        return date_time;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDate_time(LocalDate date_time) {
        this.date_time = date_time;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", date_time=" + date_time +
                '}';
    }
}
