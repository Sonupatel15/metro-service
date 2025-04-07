//package org.example.metroservice.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.metroservice.dto.request.TimingRequest;
//import org.example.metroservice.model.Timing;
//import org.example.metroservice.repository.TimingRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.sql.Timestamp;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class TimingService {
//
//    private final TimingRepository timingRepository;
//    private final RestTemplate restTemplate;
//
//    @Value("${user.service.url}")
//    private String userServiceUrl; // e.g., http://localhost:8081
//
//    @Transactional
//    public void checkIn(TimingRequest request) {
//        UUID travelId = request.getTravelId();
//
//        if (timingRepository.findByTravelId(travelId) != null) {
//            throw new IllegalArgumentException("Check-in already recorded for travelId: " + travelId);
//        }
//
//        Timing timing = Timing.builder()
//                .travelId(travelId)
//                .checkin(new Timestamp(System.currentTimeMillis()))
//                .build();
//
//        timingRepository.save(timing);
//
//        // Notify User Service (STARTED)
//        notifyUserService(travelId, "start");
//    }
//
//    @Transactional
//    public void checkOut(TimingRequest request) {
//        UUID travelId = request.getTravelId();
//
//        Timing timing = timingRepository.findByTravelId(travelId);
//        if (timing == null) {
//            throw new IllegalArgumentException("No check-in found for travelId: " + travelId);
//        }
//
//        if (timing.getCheckout() != null) {
//            throw new IllegalArgumentException("Checkout already recorded for travelId: " + travelId);
//        }
//
//        timing.setCheckout(new Timestamp(System.currentTimeMillis()));
//        timingRepository.save(timing);
//
//        // Notify User Service (COMPLETED)
//        notifyUserService(travelId, "complete");
//    }
//
//    private void notifyUserService(UUID travelId, String status) {
//        try {
//            String url = userServiceUrl + "/api/travel-history/status/" + status + "/" + travelId;
//            restTemplate.put(url, null);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to notify User Service: " + e.getMessage());
//        }
//    }
//}


package org.example.metroservice.service;

import lombok.RequiredArgsConstructor;
import org.example.metroservice.dto.request.TimingRequest;
import org.example.metroservice.dto.response.TimingResponse;
import org.example.metroservice.model.Timing;
import org.example.metroservice.repository.TimingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimingService {

    private final TimingRepository timingRepository;
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl; // e.g., http://localhost:8081

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

        Timestamp checkoutTime = new Timestamp(System.currentTimeMillis());
        timing.setCheckout(checkoutTime);
        timingRepository.save(timing);

        // Notify User Service to update status to COMPLETED
        notifyUserService(travelId, "complete");

        return TimingResponse.builder()
                .travelId(travelId)
                .checkin(timing.getCheckin())
                .checkout(checkoutTime)
                .status("COMPLETED")
                .message("Checkout recorded successfully.")
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
}