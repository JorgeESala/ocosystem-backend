package com.ocosur.ocosystem.core.employee.model;

import java.util.List;

import com.ocosur.ocosystem.core.employee.enums.JobPosition;
import com.ocosur.ocosystem.security.model.EmployeeBusiness;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    @Column(nullable = false)
    private String role = "USER";

    @OneToMany(
        mappedBy = "employee",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private List<EmployeeBusiness> businesses = new ArrayList<>();

    @Column(nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private JobPosition position;
}
