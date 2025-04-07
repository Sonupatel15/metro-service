package org.example.metroservice.repository;

import org.example.metroservice.model.Timing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TimingRepository extends JpaRepository<Timing, UUID> {
    boolean existsByTravelId(UUID travelId);
    Timing findByTravelId(UUID travelId);
}