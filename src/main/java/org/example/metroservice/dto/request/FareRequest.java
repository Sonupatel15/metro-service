package org.example.metroservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FareRequest {
    private int fromStationId;
    private int toStationId;
    private double distance;
    private boolean isActive;
}