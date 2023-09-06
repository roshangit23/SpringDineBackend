package com.hotelPro.hotelmanagementsystem.service.operations;

import com.hotelPro.hotelmanagementsystem.model.Employee;
import com.hotelPro.hotelmanagementsystem.service.EmployeeService;
import org.springframework.stereotype.Service;
@Service
public class EmployeeOps {
    private final EmployeeService employeeService;

    public EmployeeOps(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    public void addEmployee(Employee employee) {
        // implement the logic to add an employee
        //employeeService.saveEmployee(employee);
    }

    public void getAllEmployees() {
        // implement the logic to get all employees
        //employeeService.getAllEmployees();
    }

    public void getEmployeeById(Long id) {
        // implement the logic to get employee by id
        employeeService.getEmployeeById(id);
    }

    public void updateEmployee(Long id) {
        // implement the logic to update an employee
        Employee employee = employeeService.getEmployeeById(id);
        employee.setFirstName("Jane");
        //employeeService.saveEmployee(employee);

    }

    public void deleteEmployee(Long id) {
        // implement the logic to delete an employee
        employeeService.deleteEmployee(id);
    }
}
