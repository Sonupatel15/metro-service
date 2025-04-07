package org.example.metroservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimingResponse {
    private UUID travelId;
    private Timestamp checkin;
    private Timestamp checkout;
    private String status; // STARTED / COMPLETED
    private String message;
}