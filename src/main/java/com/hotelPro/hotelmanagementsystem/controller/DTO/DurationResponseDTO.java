package com.hotelPro.hotelmanagementsystem.controller.DTO;

public class DurationResponseDTO {
    private String message;
    private Long durationInMinutes;

    public DurationResponseDTO(String message, Long durationInMinutes) {
        this.message = message;
        this.durationInMinutes = durationInMinutes;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}
