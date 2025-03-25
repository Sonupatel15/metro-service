package org.example.metroservice.service;

import org.example.metroservice.dto.request.MetroStationRequest;
import org.example.metroservice.dto.response.MetroStationResponse;
import org.example.metroservice.exception.MetroStationNotFoundException;
import org.example.metroservice.model.MetroStation;
import org.example.metroservice.repository.MetroStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetroStationService {

    private final MetroStationRepository metroStationRepository;

    public MetroStationService(MetroStationRepository metroStationRepository) {
        this.metroStationRepository = metroStationRepository;
    }

    public void addStation(MetroStationRequest request) {
        MetroStation station = new MetroStation();
        station.setStationName(request.getStationName());
        metroStationRepository.save(station);
    }

    public MetroStationResponse getStationById(int id) {
        MetroStation station = metroStationRepository.findById(id)
                .orElseThrow(() -> new MetroStationNotFoundException("Station not found with ID: " + id));
        return convertToResponse(station);
    }

    public List<MetroStationResponse> getAllStations() {
        List<MetroStation> stations = metroStationRepository.findAllByOrderByStationNameAsc();
        return stations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void updateStation(int id, MetroStationRequest request) {
        MetroStation station = metroStationRepository.findById(id)
                .orElseThrow(() -> new MetroStationNotFoundException("Station not found with ID: " + id));
        station.setStationName(request.getStationName());
        metroStationRepository.save(station);
    }

    public void deleteStation(int id) {
        if (!metroStationRepository.existsById(id)) {
            throw new MetroStationNotFoundException("Cannot delete. Station not found with ID: " + id);
        }
        metroStationRepository.deleteById(id);
    }

    private MetroStationResponse convertToResponse(MetroStation station) {
        MetroStationResponse response = new MetroStationResponse();
        response.setId(station.getId());
        response.setStationName(station.getStationName());
        return response;
    }
}


