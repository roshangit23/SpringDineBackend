package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.exception.ResourceNotFoundException;
import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.Employee;
import com.hotelPro.hotelmanagementsystem.repository.CompanyRepository;
import com.hotelPro.hotelmanagementsystem.repository.EmployeeRepository;
import com.hotelPro.hotelmanagementsystem.service.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Override
    @Transactional
    public Employee saveEmployee(Employee employee, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));
        employee.setCompany(company);
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }

    @Override
    @Transactional
    public List<Employee> getAllEmployees(Long companyId) {
        return employeeRepository.findByCompanyId(companyId);
        //return employeeRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        employeeRepository.delete(employee);
    }

    @Override
    public Employee updateEmployee(Long id, Employee employeeDetails){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        if (employeeDetails.getFirstName() != null && !employeeDetails.getFirstName().isEmpty()) {
            employee.setFirstName(employeeDetails.getFirstName());
        }
        if (employeeDetails.getLastName() != null && !employeeDetails.getLastName().isEmpty()) {
            employee.setLastName(employeeDetails.getLastName());
        }
        if (employeeDetails.getSalary() != 0.0) { // assuming 0.0 is not a valid salary
            employee.setSalary(employeeDetails.getSalary());
        }
        if (employeeDetails.getPosition() != null && !employeeDetails.getPosition().isEmpty()) {
            employee.setPosition(employeeDetails.getPosition());
        }

        if (employeeDetails.getPhoneNumber() != null && !employeeDetails.getPhoneNumber().isEmpty()) {
            employeeDetails.setPhoneNumber(employeeDetails.getPhoneNumber());
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return updatedEmployee;
    }
}
