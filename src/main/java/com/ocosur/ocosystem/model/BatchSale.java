package com.ocosur.ocosystem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class BatchSale {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;
    private BigDecimal quantitySold;
    private BigDecimal kgTotal;
    private BigDecimal saleTotal;
    private BigDecimal kgGut;
    private LocalDate date;
}
