package com.ocosur.ocosystem.core.employee.mapper;

import org.springframework.stereotype.Component;
import java.util.List;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.employee.dto.EmployeeResponseDTO;
@Component
public class EmployeeMapper {

    public EmployeeResponseDTO toResponse(Employee employee) {
        if (employee == null)
            return null;

        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setRole(employee.getRole());
        dto.setActive(employee.isActive());
        dto.setPosition(employee.getPosition());

        return dto;
    }

    public List<EmployeeResponseDTO> toResponseList(List<Employee> employees) {
        return employees.stream()
                .map(this::toResponse)
                .toList();
    }
}
