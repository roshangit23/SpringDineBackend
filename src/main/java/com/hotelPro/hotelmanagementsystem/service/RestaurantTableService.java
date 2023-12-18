package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.model.RestaurantTable;

import java.util.List;

public interface RestaurantTableService {
    RestaurantTable createTable(String category, int tableNumber, Long companyId);
    RestaurantTable createOrderForTable(Long tableId);
    List<RestaurantTable> checkAndFreeTablesIfEmpty(Long companyId);
    RestaurantTable occupyTable(Long tableId);
    RestaurantTable completeTable(Long tableId);
    RestaurantTable freeTable(Long tableId);
    RestaurantTable.TableStatus getTableStatusById(Long tableId);
    double mergeOrders(List<Long> tableIds);
    List<RestaurantTable> getAllTables(Long companyId);
    RestaurantTable getTableById(Long tableId);
    RestaurantTable updateTable(Long tableId, String category, Integer tableNumber);
    void deleteRestaurantTable(Long id);
    RestaurantTable findByTableNumberCategoryAndCompanyId(Integer tableNumber, String category, Long companyId);
}
