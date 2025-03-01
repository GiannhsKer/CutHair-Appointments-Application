package com.barbershop.CutHair.appointment;

import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
@Table
public class Appointment {

    @Id
    private String id;
    private String name;
    private String phoneNumber;
    private LocalDate dateTime;

    public Appointment() {
    }

    public Appointment(String id, String name, String phoneNumber, LocalDate dateTime) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getdateTime() {
        return dateTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setdateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", dateTime=" + dateTime +
                '}';
    }
}
