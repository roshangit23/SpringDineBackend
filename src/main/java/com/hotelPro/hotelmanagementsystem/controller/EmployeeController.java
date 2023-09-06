package com.hotelPro.hotelmanagementsystem.controller;

import com.hotelPro.hotelmanagementsystem.controller.DTO.EmployeeResponseDTO;
import com.hotelPro.hotelmanagementsystem.model.Employee;
import com.hotelPro.hotelmanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/createEmployee/{companyId}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> saveEmployee(@Valid @RequestBody Employee employee, @PathVariable Long companyId) {
        Employee createdEmployee = employeeService.saveEmployee(employee, companyId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), new EmployeeResponseDTO(createdEmployee)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new EmployeeResponseDTO(employee)));
    }

    @GetMapping("/getAll/{companyId}")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDTO>>> getAllEmployees(@PathVariable Long companyId) {
        List<Employee> employees = employeeService.getAllEmployees(companyId);
        List<EmployeeResponseDTO> employeeDTOs = employees.stream()
                .map(EmployeeResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), employeeDTOs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> updateEmployee(@RequestBody Employee employee, @PathVariable Long id) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), new EmployeeResponseDTO(updatedEmployee)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Employee deleted successfully"));
    }
}

