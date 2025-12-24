package com.ocosur.ocosystem.core.employee.model;

import com.ocosur.ocosystem.core.employee.enums.JobPosition;

import jakarta.persistence.*;
import lombok.*;

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

    @Column( unique = true)
    private String email;

    private String passwordHash;

    @Column(nullable = false)
    private String role = "USER";


    @Column(nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private JobPosition position;
}
