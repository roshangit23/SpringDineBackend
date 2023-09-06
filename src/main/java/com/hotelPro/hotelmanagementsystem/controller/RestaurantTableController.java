package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.*;
import com.hotelPro.hotelmanagementsystem.exception.CustomException;
import com.hotelPro.hotelmanagementsystem.filter.ValidateMergeOrdersRequest;
import com.hotelPro.hotelmanagementsystem.model.Order;
import com.hotelPro.hotelmanagementsystem.model.RestaurantTable;
import com.hotelPro.hotelmanagementsystem.service.RestaurantTableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tables")
public class RestaurantTableController {

    private final RestaurantTableService tableService;

    @Autowired
    public RestaurantTableController(RestaurantTableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/createTable/{companyId}")
    public ResponseEntity<ApiResponse<RestaurantTableResponseDTO>> createTable(@Valid @RequestBody RestaurantTableRequestDTO request, @PathVariable Long companyId) {
        RestaurantTable restaurantTable = tableService.createTable(request.getCategory(), request.getTableNumber(), companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), new RestaurantTableResponseDTO(restaurantTable)));
    }

    @PostMapping("/createOrderForTable/{tableId}")
    public ResponseEntity<ApiResponse<TableOrderResponseDTO>> createOrderForTable(@PathVariable Long tableId) {
        RestaurantTable updatedTable = tableService.createOrderForTable(tableId);
        Order order = updatedTable.getCurrentOrder();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), new TableOrderResponseDTO(order)));
    }

    @GetMapping("/getAll/{companyId}")
    public ResponseEntity<ApiResponse<List<RestaurantTableResponseDTO>>> getAllTables(@PathVariable Long companyId) {
        List<RestaurantTable> tables = tableService.getAllTables(companyId);
        List<RestaurantTableResponseDTO> tableDTOs = tables.stream().map(RestaurantTableResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), tableDTOs));
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<ApiResponse<RestaurantTableResponseDTO>> getTableById(@PathVariable Long tableId) {
        RestaurantTable table = tableService.getTableById(tableId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new RestaurantTableResponseDTO(table)));
    }

    @GetMapping("/status/{tableId}")
    public ResponseEntity<ApiResponse<TableStatusDTO>> getTableStatus(@PathVariable Long tableId) {
        RestaurantTable.TableStatus status = tableService.getTableStatusById(tableId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new TableStatusDTO(status)));
    }

    @PutMapping("/{tableId}")
    public ResponseEntity<ApiResponse<RestaurantTableResponseDTO>> updateTable(@PathVariable Long tableId, @RequestBody RestaurantTableRequestDTO request) {
        RestaurantTable restaurantTable = tableService.updateTable(tableId, request.getCategory(), request.getTableNumber());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new RestaurantTableResponseDTO(restaurantTable)));
    }

    @PutMapping("/checkAndFreeTablesIfEmpty/{companyId}")
    public ResponseEntity<ApiResponse<List<RestaurantTableResponseDTO>>> checkAndFreeTablesIfEmpty(@PathVariable Long companyId) {
        List<RestaurantTable> tables = tableService.checkAndFreeTablesIfEmpty(companyId);
        List<RestaurantTableResponseDTO> tableDTOs = tables.stream().map(RestaurantTableResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), tableDTOs));
    }

    @PutMapping("/occupy/{tableId}")
    public ResponseEntity<ApiResponse<RestaurantTableResponseDTO>> occupyTable(@PathVariable Long tableId) {
        RestaurantTable table = tableService.occupyTable(tableId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new RestaurantTableResponseDTO(table)));
    }

    @PutMapping("/complete/{tableId}")
    public ResponseEntity<ApiResponse<RestaurantTableResponseDTO>> completeTable(@PathVariable Long tableId) {
        RestaurantTable table = tableService.completeTable(tableId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new RestaurantTableResponseDTO(table)));
    }

    @PutMapping("/free/{tableId}")
    public ResponseEntity<ApiResponse<RestaurantTableResponseDTO>> freeTable(@PathVariable Long tableId) {
        RestaurantTable table = tableService.freeTable(tableId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new RestaurantTableResponseDTO(table)));
    }

    @PostMapping("/mergeOrders")
    @ValidateMergeOrdersRequest
    public ResponseEntity<ApiResponse<Double>> mergeOrders(@RequestBody MergeOrdersRequestDTO mergeOrdersRequest) {
        List<Long> tableIds = mergeOrdersRequest.getTableIds();

        if (tableIds == null || tableIds.isEmpty()) {
            throw new CustomException("List of table ids cannot be null or empty", HttpStatus.BAD_REQUEST);
        }

        if (tableIds.size() < 2) {
            throw new CustomException("At least two table ids must be provided to merge orders", HttpStatus.BAD_REQUEST);
        }

        double totalAmount = tableService.mergeOrders(tableIds);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), totalAmount));
    }
}
