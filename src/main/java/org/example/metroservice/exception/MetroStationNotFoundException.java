package org.example.metroservice.exception;

public class MetroStationNotFoundException extends RuntimeException {
    public MetroStationNotFoundException(String message) {
        super(message);
    }
}