package org.example.metroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "fare_chart")
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate an all-arguments constructor
public class Fare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "from_station_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_farechart_from_station"))
    @OnDelete(action = OnDeleteAction.CASCADE) // Use Hibernate annotation
    private MetroStation fromStation;

    @ManyToOne
    @JoinColumn(name = "to_station_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_farechart_to_station"))
    @OnDelete(action = OnDeleteAction.CASCADE) // Use Hibernate annotation
    private MetroStation toStation;


    @Column(nullable = false)
    private double distance;

    @Column(nullable = false)
    private boolean isActive;
}
