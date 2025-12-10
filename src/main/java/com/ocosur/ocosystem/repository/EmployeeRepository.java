package com.ocosur.ocosystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    
}
