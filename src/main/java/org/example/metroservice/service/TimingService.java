package org.example.metroservice.service;

import lombok.RequiredArgsConstructor;
import org.example.metroservice.dto.request.PenaltyRequest;
import org.example.metroservice.dto.request.TimingRequest;
import org.example.metroservice.dto.response.PenaltyResponse;
import org.example.metroservice.dto.response.PenaltyStatusResponse;
import org.example.metroservice.dto.response.TimingResponse;
import org.example.metroservice.model.Timing;
import org.example.metroservice.repository.TimingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimingService {

    private final TimingRepository timingRepository;
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl; //
    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    private void validateTravelIdExists(UUID travelId) {
        String url = userServiceUrl + "/api/travel-history/exists/" + travelId;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
        System.out.println("User Service response: " + response);
        if (!Boolean.TRUE.equals(response.getBody())) {
            throw new IllegalArgumentException("Travel ID not found in User Service.");
        }
    }

    @Transactional
    public TimingResponse checkIn(TimingRequest request) {
        UUID travelId = request.getTravelId();
        validateTravelIdExists(travelId);

        if (timingRepository.existsByTravelId(travelId)) {
            throw new IllegalArgumentException("Check-in already recorded for travelId: " + travelId);
        }

        Timestamp checkinTime = new Timestamp(System.currentTimeMillis());

        Timing timing = Timing.builder()
                .travelId(travelId)
                .checkin(checkinTime)
                .build();

        timingRepository.save(timing);

        // Notify User Service to update status to STARTED
        notifyUserService(travelId, "start");

        return TimingResponse.builder()
                .travelId(travelId)
                .checkin(checkinTime)
                .status("STARTED")
                .message("Check-in recorded successfully.")
                .build();
    }

    private void notifyUserService(UUID travelId, String status) {
        try {
            String url = userServiceUrl + "/api/travel-history/status/" + status + "/" + travelId;
            restTemplate.put(url, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to notify User Service: " + e.getMessage());
        }
    }

    public TimingResponse getTimingById(UUID timingId) {
        Timing timing = timingRepository.findById(timingId)
                .orElseThrow(() -> new RuntimeException("Timing not found"));

        return TimingResponse.builder()
                .timingId(timing.getTimingId())
                .travelId(timing.getTravelId())
                .checkin(timing.getCheckin())
                .checkout(timing.getCheckout())
                .build();
    }

    @Transactional
    public TimingResponse checkOut(TimingRequest request) {
        UUID travelId = request.getTravelId();
        validateTravelIdExists(travelId);

        Timing timing = timingRepository.findByTravelId(travelId);
        if (timing == null) {
            throw new IllegalArgumentException("No check-in found for travelId: " + travelId);
        }

        if (timing.getCheckout() != null) {
            throw new IllegalArgumentException("Checkout already recorded for travelId: " + travelId);
        }

        // Record checkout time first
        Timestamp checkoutTime = new Timestamp(System.currentTimeMillis());
        timing.setCheckout(checkoutTime);
        timingRepository.save(timing);

        // Calculate duration in minutes
        long durationMinutes = Duration.between(
                timing.getCheckin().toInstant(),
                checkoutTime.toInstant()
        ).toMinutes();

        // Apply penalty if duration > 90 minutes
        if (durationMinutes > 90) {
            try {
                // Call Penalty Service
                PenaltyRequest penaltyRequest = new PenaltyRequest();
                penaltyRequest.setTravelId(travelId);
                penaltyRequest.setTimingId(timing.getTimingId());

                ResponseEntity<PenaltyResponse> penaltyResponse = restTemplate.postForEntity(
                        paymentServiceUrl + "/penalties",
                        penaltyRequest,
                        PenaltyResponse.class
                );

                if (!penaltyResponse.getStatusCode().is2xxSuccessful()) {
                    // Log penalty failure but don't block checkout
                    System.err.println("Penalty application failed but checkout completed");
                }
            } catch (Exception e) {
                System.err.println("Error calling penalty service: " + e.getMessage());
            }
        }

        // Update travel status to COMPLETED (regardless of penalty)
        notifyUserService(travelId, "complete");

        return TimingResponse.builder()
                .timingId(timing.getTimingId())
                .travelId(travelId)
                .checkin(timing.getCheckin())
                .checkout(checkoutTime)
                .status("COMPLETED")
                .message("Checkout recorded successfully" +
                        (durationMinutes > 90 ? " (Penalty applied)" : ""))
                .build();
    }
}