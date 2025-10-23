package com.ocosur.ocosystem.model;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Batch {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Branch branch;
    private BigDecimal pricePerKg;
    private Date date;
    private String provider;
    private BigDecimal chickenQuantity;
    private BigDecimal priceTotal;
    private BigDecimal avgChickenWeight;
}
