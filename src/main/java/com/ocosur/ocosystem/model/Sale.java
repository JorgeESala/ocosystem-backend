package com.ocosur.ocosystem.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Sale {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
    @JoinColumn(name = "ticket_id")
	@JsonIgnore
	private Ticket ticket;
    
	private BigDecimal subtotal;

	private BigDecimal price;

	@ManyToOne
    @JoinColumn(name = "barcode_product")
	private Product product;

	private BigDecimal quantity;

	private Integer batch;
}