package com.ocosur.ocosystem.security.model;

import com.ocosur.ocosystem.core.employee.model.Employee;

import lombok.Data;

@Data
public class AuthenticatedUser {

    private final String token;
    private final Employee employee;
}
