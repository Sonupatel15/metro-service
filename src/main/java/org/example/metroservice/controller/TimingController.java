package org.example.metroservice.controller;

import org.example.metroservice.dto.request.TimingRequest;
import org.example.metroservice.dto.response.TimingResponse;
import org.example.metroservice.service.TimingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/timings")
@RequiredArgsConstructor
public class TimingController {

    private final TimingService timingService;

    @PostMapping("/checkin")
    public ResponseEntity<TimingResponse> checkIn(@RequestBody TimingRequest request) {
        TimingResponse response = timingService.checkIn(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout")
    public ResponseEntity<TimingResponse> checkOut(@RequestBody TimingRequest request) {
        TimingResponse response = timingService.checkOut(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{timingId}")
    public ResponseEntity<TimingResponse> getTiming(@PathVariable UUID timingId) {
        TimingResponse response = timingService.getTimingById(timingId);
        return ResponseEntity.ok(response);
    }

}