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
public class DailyBatchSale {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Batch batch;
    private BigDecimal quantitySold;
    private BigDecimal kgTotal;
    private BigDecimal saleTotal;
    private BigDecimal kgGut;
    private Date date;
}
