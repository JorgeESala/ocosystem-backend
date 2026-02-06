package com.ocosur.ocosystem.livechicken.accounting.compensation_payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(schema = "live_chicken")
public class CompensationPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private AccountingEntity branch;

    @ManyToOne(optional = false)
    private AccountingEntity supplier;

    @ManyToOne(optional = false)
    private AccountingEntity cedis;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    OffsetDateTime createdAt;

    @Column(nullable = false)
    LocalDate date;

    @Column(nullable = false, unique = true)
    private String folio;

    @Column(nullable = true)
    private String note;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
}
