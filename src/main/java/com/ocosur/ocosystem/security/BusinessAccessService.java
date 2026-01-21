package com.ocosur.ocosystem.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessAccessService {

    private final EmployeeRepository employeeRepo;

    public boolean hasBusinessAccess(String businessCode) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        Employee employee = employeeRepo
                .findByEmail(email)
                .orElseThrow();

        return employee.getBusinesses()
                .stream()
                .anyMatch(b -> b.getBusinessType()
                        .getCode()
                        .equals(businessCode));
    }
}
