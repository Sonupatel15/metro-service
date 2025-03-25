package org.example.metroservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "metro_stations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetroStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private int id;

    @Column(name = "station_name", nullable = false, unique = true)
    private String stationName;

    @OneToMany(mappedBy = "fromStation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fare> fareFrom;

    @OneToMany(mappedBy = "toStation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fare> fareTo;
}