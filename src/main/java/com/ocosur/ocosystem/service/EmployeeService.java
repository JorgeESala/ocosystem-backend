package com.ocosur.ocosystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.model.Employee;
import com.ocosur.ocosystem.repository.EmployeeRepository;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
