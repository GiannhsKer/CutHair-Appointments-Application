package com.barbershop.CutHair.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    @Query("select a from Appointment a where a.id = ?1")
    Optional<Appointment> findById(String uuid);
}
