package com.gi.cuthair.appointment.repository;

import com.gi.cuthair.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("SELECT a FROM Appointment a WHERE a.name = ?1")
    Optional<Appointment> findByName(String name);

    @Query("SELECT a FROM Appointment a WHERE a.phoneNumber = ?1")
    Optional<Appointment> findByPhone(String phoneNumber);

    @Query("SELECT a FROM Appointment a WHERE a.dateTime BETWEEN :startOfDay AND :endOfDay")
    List<Appointment> findByDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT a FROM Appointment a WHERE a.dateTime > :startTime AND a.dateTime < :endTime AND (:id IS NULL OR a.id <> :id)")
    List<Appointment> findByDateRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("id") UUID id);

}
