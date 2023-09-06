package com.hotelPro.hotelmanagementsystem.controller.DTO;

import java.util.List;

public class MergeOrdersRequestDTO {
    private List<Long> tableIds;

    // Getters and setters


    public List<Long> getTableIds() {
        return tableIds;
    }

    public void setTableIds(List<Long> tableIds) {
        this.tableIds = tableIds;
    }
}
