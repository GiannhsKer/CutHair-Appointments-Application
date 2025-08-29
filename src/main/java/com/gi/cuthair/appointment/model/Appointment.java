package com.gi.cuthair.appointment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments_spring")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // required by JPA, but protected so not misused
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)  // avoid lazy-loading problems
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // safer equality (by id only)
public class Appointment {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    @Setter
    private String name;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^69[0-9]{8}$", message = "Phone number must be exactly 10 digits")
    @Column(nullable = false, unique = true)
    @Setter
    private String phoneNumber;
    
    @NotNull(message = "Date and time is required")
    @Future(message = "Appointment must be in the future")
    @Column(nullable = false)
    @Setter
    private LocalDateTime dateTime;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Appointment(String name, String phoneNumber, LocalDateTime dateTime) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dateTime = dateTime;
    }
}
