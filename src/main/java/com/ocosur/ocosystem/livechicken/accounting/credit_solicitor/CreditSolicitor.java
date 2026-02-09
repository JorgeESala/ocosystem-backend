package com.ocosur.ocosystem.livechicken.accounting.credit_solicitor;

import java.time.OffsetDateTime;

import org.hibernate.annotations.SQLRestriction;
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
@SQLRestriction("active = true")
public class CreditSolicitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private AccountingEntity accountingEntity;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
}
