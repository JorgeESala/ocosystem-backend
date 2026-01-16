package com.ocosur.ocosystem.livechicken.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "LiveChickenExpense")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "expense", schema = "live_chicken")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ExpenseCategoryCode categoryCode;

    private BigDecimal amount;

    private LocalDate date;

    @OneToOne(mappedBy = "expense")
    private FoodExpense foodExpense;

    @OneToOne(mappedBy = "expense")
    private VehicleExpense vehicleExpense;

    @OneToOne(mappedBy = "expense")
    private FuelExpense fuelExpense;

}
