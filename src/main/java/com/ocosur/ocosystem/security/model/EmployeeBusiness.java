package com.ocosur.ocosystem.security.model;

import com.ocosur.ocosystem.core.employee.model.Employee;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_business", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "employee_id", "business_type_id" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeBusiness {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "business_type_id", nullable = false)
    private BusinessType businessType;
}
