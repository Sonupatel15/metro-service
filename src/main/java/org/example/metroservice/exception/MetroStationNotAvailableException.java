package org.example.metroservice.exception;

public class MetroStationNotAvailableException extends RuntimeException {
    public MetroStationNotAvailableException(String message) {
        super(message);
    }
}