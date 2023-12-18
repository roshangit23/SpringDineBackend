package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee,Long companyId);
    Employee getEmployeeById(Long id);
    List<Employee> getAllEmployees(Long companyId);
    void deleteEmployee(Long id);
    Employee updateEmployee(Long id, Employee employee);
}
