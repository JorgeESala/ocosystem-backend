package com.ocosur.ocosystem.core.employee.dto;

import com.ocosur.ocosystem.core.employee.enums.JobPosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean active;
    private JobPosition position;
}
