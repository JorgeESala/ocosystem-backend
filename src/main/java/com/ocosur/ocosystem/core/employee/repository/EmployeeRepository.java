package com.ocosur.ocosystem.core.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.core.employee.enums.JobPosition;
import com.ocosur.ocosystem.core.employee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByRole(String role);
    List<Employee> findByPosition(JobPosition position);

    boolean existsByEmail(String email);

}
