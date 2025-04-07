package org.example.metroservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFareStatusRequest {
    private int fromStationId;
    private int toStationId;
    private boolean isActive;
}