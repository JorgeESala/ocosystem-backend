package com.ocosur.ocosystem.livechicken.expense;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.vehicle.model.Vehicle;
import com.ocosur.ocosystem.livechicken.route.Route;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(schema = "live_chicken", name = "fuel_expense")
@Data
public class FuelExpense {

    @Id
    private Long expenseId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
}
