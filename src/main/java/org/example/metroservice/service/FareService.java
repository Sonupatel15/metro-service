package org.example.metroservice.service;

import org.example.metroservice.dto.request.FareRequest;
import org.example.metroservice.dto.request.UpdateFareStatusRequest;
import org.example.metroservice.exception.MetroStationNotAvailableException;
import org.example.metroservice.exception.MetroStationNotFoundException;
import org.example.metroservice.model.Fare;
import org.example.metroservice.model.MetroStation;
import org.example.metroservice.repository.FareRepository;
import org.example.metroservice.repository.MetroStationRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class FareService {

    private final FareRepository fareChartRepository;
    private final MetroStationRepository metroStationRepository;

    public FareService(FareRepository fareChartRepository, MetroStationRepository metroStationRepository) {
        this.fareChartRepository = fareChartRepository;
        this.metroStationRepository = metroStationRepository;
    }

    // ✅ Add Fare Entry
    public void addFareChart(FareRequest request) {
        MetroStation fromStation = metroStationRepository.findById(request.getFromStationId())
                .orElseThrow(() -> new MetroStationNotFoundException("From Station ID " + request.getFromStationId() + " not found."));
        MetroStation toStation = metroStationRepository.findById(request.getToStationId())
                .orElseThrow(() -> new MetroStationNotFoundException("To Station ID " + request.getToStationId() + " not found."));

        Fare fareChart = new Fare();
        fareChart.setFromStation(fromStation);
        fareChart.setToStation(toStation);
        fareChart.setDistance(request.getDistance());
        fareChart.setActive(request.isActive());

        fareChartRepository.save(fareChart);
    }

    // ✅ Get Distance Between Two Stations
    public double getDistanceBetweenStations(int fromStationId, int toStationId) {
        MetroStation fromStation = metroStationRepository.findById(fromStationId)
                .orElseThrow(() -> new MetroStationNotFoundException("From Station ID " + fromStationId + " not found."));
        MetroStation toStation = metroStationRepository.findById(toStationId)
                .orElseThrow(() -> new MetroStationNotFoundException("To Station ID " + toStationId + " not found."));

        // ✅ Check active status from Fare entity, NOT MetroStation
        Optional<Fare> fareChart = fareChartRepository.findByFromStationIdAndToStationId(fromStationId, toStationId);

        Fare fare = fareChart.orElseThrow(() ->
                new MetroStationNotFoundException("No fare chart entry found for these stations."));

        if (!fare.isActive()) { // ✅ Fix: Checking isActive from Fare, not MetroStation
            throw new MetroStationNotAvailableException("This route is currently not available.");
        }

        return fare.getDistance();
    }

    // ✅ Update Active Status of a Route
    public void updateFareChartStatus(UpdateFareStatusRequest request) {
        Fare fareChart = fareChartRepository.findByFromStationIdAndToStationId(
                        request.getFromStationId(), request.getToStationId())
                .orElseThrow(() -> new MetroStationNotFoundException("Fare route between stations not found."));

        fareChart.setActive(request.isActive());
        fareChartRepository.save(fareChart);
    }
}


