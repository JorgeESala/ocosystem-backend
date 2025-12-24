package com.ocosur.ocosystem.livechicken.inboundbatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.ocosur.ocosystem.livechicken.supplier.Supplier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "inbound_batch", schema = "live_chicken")
public class InboundBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    private LocalDate date;
    private BigDecimal realWeight;
    private BigDecimal declaredWeight;
    private Integer chickenQuantity;
    private BigDecimal pricePerKg;
    @Column(insertable = false, updatable = false)
    private BigDecimal totalPaid;
    @Column(insertable = false, updatable = false)
    private BigDecimal avgWeight;
}
