package org.example.metroservice.repository;

import org.example.metroservice.model.Fare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FareRepository extends JpaRepository<Fare, Integer> {
    Optional<Fare> findByFromStationIdAndToStationId(int fromStationId, int toStationId);
}
