package com.ocosur.ocosystem.core.employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.core.employee.dto.EmployeeResponseDTO;
import com.ocosur.ocosystem.core.employee.enums.JobPosition;
import com.ocosur.ocosystem.core.employee.mapper.EmployeeMapper;
import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.employee.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    EmployeeMapper employeeMapper;;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<EmployeeResponseDTO> getByPosition(JobPosition position) {
        List<Employee> employees = employeeRepository.findByPosition(position);

        return employees.stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    public List<EmployeeResponseDTO> getEmployees(JobPosition position) {
        List<Employee> employees;

        if (position != null) {
            employees = employeeRepository.findByPosition(position);
        } else {
            employees = employeeRepository.findAll();
        }

        return employees.stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    public Employee getById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException(
            "Empleado con el id " + employeeId + " no encontrado"
        ));
    }

}
