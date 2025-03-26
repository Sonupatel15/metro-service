package org.example.metroservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "station_managers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StationManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private int id;

    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false)
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Mobile number cannot be blank")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number format")
    @Column(nullable = false, unique = true)
    private String mobile;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false) // FK to MetroStation
    private MetroStation metroStation;
}