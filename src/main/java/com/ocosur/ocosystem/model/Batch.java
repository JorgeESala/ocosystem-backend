package com.ocosur.ocosystem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Batch {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    private BigDecimal kgTotal;
    private BigDecimal pricePerKg;
    private LocalDate date;
    private String provider;
    private BigDecimal chickenQuantity;
    @Column(insertable = false, updatable = false)
    private BigDecimal priceTotal;
    @Column(insertable = false, updatable = false)
    private BigDecimal avgChickenWeight;
}
