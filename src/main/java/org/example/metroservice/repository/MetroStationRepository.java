package org.example.metroservice.repository;

import org.example.metroservice.model.MetroStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetroStationRepository extends JpaRepository<MetroStation, Integer> {

    List<MetroStation> findAllByOrderByStationNameAsc();

}