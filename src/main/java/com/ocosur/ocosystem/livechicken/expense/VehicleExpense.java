package com.ocosur.ocosystem.livechicken.expense;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.vehicle.model.Vehicle;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(schema = "live_chicken", name = "vehicle_expense")
@Getter
@Setter
public class VehicleExpense {

    @Id
    private Long expenseId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false, length = 30)
    private VehicleExpenseCategory category;
}
