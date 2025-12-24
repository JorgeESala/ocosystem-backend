package com.ocosur.ocosystem.core.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.core.employee.dto.EmployeeResponseDTO;
import com.ocosur.ocosystem.core.employee.enums.JobPosition;
import com.ocosur.ocosystem.core.employee.service.EmployeeService;
@RestController
@CrossOrigin
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployees(
            @RequestParam(required = false) JobPosition position
    ) {
        return ResponseEntity.ok(employeeService.getEmployees(position));
    }
    
    
}
