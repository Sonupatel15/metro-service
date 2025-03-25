package org.example.metroservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FareResponse {
    private int id;
    private int fromStationId;
    private String fromStationName;
    private int toStationId;
    private String toStationName;
    private double distance;
    private boolean isActive;
}