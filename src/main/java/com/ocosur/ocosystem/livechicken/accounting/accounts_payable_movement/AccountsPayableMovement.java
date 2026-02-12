package com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;


import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.AccountsPayable;
import com.ocosur.ocosystem.livechicken.accounting.compensation_payment.CompensationPayment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
@Table(schema = "live_chicken", indexes = {
        @Index(name = "idx_apm_accounts_payable", columnList = "accounts_payable_id"),
        @Index(name = "idx_apm_created_at", columnList = "created_at"),
        @Index(name = "idx_apm_source", columnList = "source_type, source_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountsPayableMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -----------------------
    // Relationship
    // -----------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accounts_payable_id", nullable = false)
    private AccountsPayable accountsPayable;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "compensation_payment_id")
    private CompensationPayment compensationPayment;

    // -----------------------
    // Movement info
    // -----------------------

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 30)
    private AccountsPayableMovementType movementType;

    @Column(name = "movement_date", nullable = false)
    private LocalDate movementDate;

    // -----------------------
    // Amounts
    // -----------------------

    /**
     * Always a positive amount.
     * Meaning depends on movementType.
     */
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;


    // -----------------------
    // Metadata
    // -----------------------

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    // -----------------------
    // JPA lifecycle
    // -----------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    // -----------------------
    // Factory methods
    // -----------------------

    public static AccountsPayableMovement payment(
            AccountsPayable ap,
            BigDecimal amount,
            LocalDate movementDate,
            String note) {
        validateAmount(amount);

        AccountsPayableMovement m = new AccountsPayableMovement();
        m.accountsPayable = ap;
        m.movementType = AccountsPayableMovementType.PAYMENT;
        m.amount = amount;
        m.movementDate = movementDate;
        m.note = note;
        return m;
    }

    public static AccountsPayableMovement adjustment(
            AccountsPayable ap,
            BigDecimal amount,
            LocalDate movementDate,
            String note) {
        validateAmount(amount);

        AccountsPayableMovement m = new AccountsPayableMovement();
        m.accountsPayable = ap;
        m.movementType = AccountsPayableMovementType.ADJUSTMENT;
        m.amount = amount;
        m.movementDate = movementDate;
        m.note = note;
        return m;
    }

    public static AccountsPayableMovement compensation(
            AccountsPayable ap,
            BigDecimal amount,
            CompensationPayment compensationPayment,
            String note) {
        validateAmount(amount);

        AccountsPayableMovement m = new AccountsPayableMovement();
        m.accountsPayable = ap;
        m.movementType = AccountsPayableMovementType.COMPENSATION;
        m.amount = amount;
        m.movementDate = compensationPayment.getDate();
        m.compensationPayment = compensationPayment;
        m.note = note;

        return m;
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
