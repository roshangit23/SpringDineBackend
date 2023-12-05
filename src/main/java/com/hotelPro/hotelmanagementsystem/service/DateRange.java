package com.hotelPro.hotelmanagementsystem.service;

import java.time.LocalDateTime;

public class DateRange {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public DateRange(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
