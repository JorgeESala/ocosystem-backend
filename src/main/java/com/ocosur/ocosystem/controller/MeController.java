package com.ocosur.ocosystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.employee.repository.EmployeeRepository;
import com.ocosur.ocosystem.dto.ChangeCredentialsRequest;
import com.ocosur.ocosystem.dto.MeResponse;
import com.ocosur.ocosystem.security.EmployeeDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public MeResponse me(@AuthenticationPrincipal EmployeeDetails principal) {
        Employee employee = principal.getEmployee();

        return new MeResponse(
                employee.getName(),
                employee.getEmail());
    }

    @PostMapping("/change-credentials")
    public ResponseEntity<Void> changeCredentials(
            @AuthenticationPrincipal EmployeeDetails principal,
            @RequestBody ChangeCredentialsRequest req) {

        Employee employee = principal.getEmployee();
        employee.setName(req.name());
        employee.setEmail(req.email().toLowerCase().trim());
        employee.setPasswordHash(
                passwordEncoder.encode(req.newPassword()));

        employeeRepository.save(employee);

        return ResponseEntity.noContent().build();
    }

}
