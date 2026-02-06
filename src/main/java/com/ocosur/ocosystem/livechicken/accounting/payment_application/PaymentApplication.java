package com.ocosur.ocosystem.livechicken.accounting.payment_application;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.AccountsPayable;
import com.ocosur.ocosystem.livechicken.accounting.payment.Payment;

@Entity
@Table(schema = "live_chicken", indexes = {
        @Index(name = "idx_pa_payment", columnList = "payment_id"),
        @Index(name = "idx_pa_accounts_payable", columnList = "accounts_payable_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -----------------------
    // Relationships
    // -----------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accounts_payable_id", nullable = false)
    private AccountsPayable accountsPayable;

    // -----------------------
    // Amount
    // -----------------------

    @Column(name = "applied_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal appliedAmount;

    // -----------------------
    // Metadata
    // -----------------------

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // -----------------------
    // JPA lifecycle
    // -----------------------

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // -----------------------
    // Factory method
    // -----------------------

    public static PaymentApplication create(
            Payment payment,
            AccountsPayable accountsPayable,
            BigDecimal appliedAmount) {
        if (appliedAmount == null || appliedAmount.signum() <= 0) {
            throw new IllegalArgumentException("Applied amount must be greater than zero");
        }

        PaymentApplication pa = new PaymentApplication();
        pa.payment = payment;
        pa.accountsPayable = accountsPayable;
        pa.appliedAmount = appliedAmount;
        return pa;
    }
}