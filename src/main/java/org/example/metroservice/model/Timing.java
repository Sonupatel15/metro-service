package org.example.metroservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "timings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID timingId;

    @Column(name = "travel_id", nullable = false, unique = true)
    private UUID travelId;  // Foreign key to User Service

    @Column(name = "checkin", nullable = false)
    private Timestamp checkin;

    @Column(name = "checkout")
    private Timestamp checkout;

}