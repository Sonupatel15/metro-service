package org.example.metroservice.controller;

import org.example.metroservice.dto.request.MetroStationRequest;
import org.example.metroservice.dto.response.MetroStationResponse;
import org.example.metroservice.service.MetroStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metro-stations")
public class MetroStationController {

    private final MetroStationService metroStationService;

    public MetroStationController(MetroStationService metroStationService) {
        this.metroStationService = metroStationService;
    }

    @PostMapping
    public ResponseEntity<String> addStation(@RequestBody MetroStationRequest request) {
        metroStationService.addStation(request);
        return ResponseEntity.ok("Metro Station successfully added.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetroStationResponse> getStationById(@PathVariable int id) {
        return ResponseEntity.ok(metroStationService.getStationById(id));
    }

    @GetMapping
    public ResponseEntity<List<MetroStationResponse>> getAllStations() {
        List<MetroStationResponse> stations = metroStationService.getAllStations();
        if (stations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStation(@PathVariable int id, @RequestBody MetroStationRequest request) {
        metroStationService.updateStation(id, request);
        return ResponseEntity.ok("Metro Station successfully updated.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStation(@PathVariable int id) {
        metroStationService.deleteStation(id);
        return ResponseEntity.ok("Metro Station successfully deleted.");
    }
}