package org.example.metroservice.controller;

import org.example.metroservice.dto.request.FareRequest;
import org.example.metroservice.dto.request.UpdateFareStatusRequest;
import org.example.metroservice.service.FareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fare-chart")
public class FareController {

    private final FareService fareChartService;

    public FareController(FareService fareChartService) {
        this.fareChartService = fareChartService;
    }

    // ✅ Add Fare Entry
    @PostMapping
    public ResponseEntity<String> addFareChart(@RequestBody FareRequest request) {
        fareChartService.addFareChart(request);
        return ResponseEntity.ok("Fare chart entry successfully added.");
    }

    // ✅ Get Distance Between Two Stations
    @GetMapping("/distance")
    public ResponseEntity<Double> getDistance(
            @RequestParam int fromStationId,
            @RequestParam int toStationId) {
        double distance = fareChartService.getDistanceBetweenStations(fromStationId, toStationId);
        return ResponseEntity.ok(distance);
    }

    // ✅ Update Active Status of a Route
    @PutMapping("/update-status")
    public ResponseEntity<String> updateFareChartStatus(@RequestBody UpdateFareStatusRequest request) {
        fareChartService.updateFareChartStatus(request);
        return ResponseEntity.ok("Fare chart status successfully updated.");
    }
}