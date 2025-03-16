package com.barbershop.CutHair.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    @Override
    @NonNull
    @Query("select a from Appointment a where a.id = ?1")
    Optional<Appointment> findById(String id);

    @Query("select a from Appointment a where a.name = ?1")
    Optional<Appointment> findByName(String name);

    @Query("select a from Appointment a where a.phoneNumber = ?1")
    Optional<Appointment> findByPhone(String phoneNumber);

    @Query("select a from Appointment a where a.dateTime between :startOfDay and :endOfDay")
    List<Appointment> findByDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
