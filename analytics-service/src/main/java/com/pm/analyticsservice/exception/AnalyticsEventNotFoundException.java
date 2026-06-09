package com.pm.analyticsservice.exception;

public class AnalyticsEventNotFoundException extends RuntimeException {
    public AnalyticsEventNotFoundException(String message) {
        super(message);
    }
}
