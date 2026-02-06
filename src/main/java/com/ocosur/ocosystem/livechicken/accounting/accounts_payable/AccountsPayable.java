package com.ocosur.ocosystem.livechicken.accounting.accounts_payable;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement.AccountsPayableMovement;
import com.ocosur.ocosystem.livechicken.accounting.common.AccountsPayableSourceType;

@Entity
@Table(schema = "live_chicken", indexes = {
        @Index(name = "idx_ap_creditor", columnList = "creditor_entity_id"),
        @Index(name = "idx_ap_debtor", columnList = "debtor_entity_id"),
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountsPayable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -----------------------
    // Accounting entities
    // -----------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creditor_entity_id", nullable = false)
    private AccountingEntity creditor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debtor_entity_id", nullable = false)
    private AccountingEntity debtor;

    // -----------------------
    // Source document
    // -----------------------

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", length = 30)
    private AccountsPayableSourceType sourceType;

    @Column(name = "source_id", nullable = true)
    private Long sourceId;

    // -----------------------
    // Amounts
    // -----------------------

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

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
    // Factory methods
    // -----------------------

    public static AccountsPayable create(
            AccountingEntity creditor,
            AccountingEntity debtor,
            BigDecimal amount,
            AccountsPayableSourceType sourceType,
            Long sourceId) {
        AccountsPayable ap = new AccountsPayable();
        ap.creditor = creditor;
        ap.debtor = debtor;
        ap.totalAmount = amount;
        ap.balance = amount;
        ap.sourceType = sourceType;
        ap.sourceId = sourceId;
        return ap;
    }

    // -----------------------
    // Domain behavior
    // -----------------------

    public void applyCharge(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.totalAmount = this.totalAmount.add(amount);
    }

    public void applyPayment(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        if (this.balance.signum() < 0) {
            throw new IllegalStateException("Accounts payable balance cannot be negative");
        }
    }

    public void apply(AccountsPayableMovement movement) {

        if (!movement.getAccountsPayable().equals(this)) {
            throw new IllegalArgumentException("Movement does not belong to this debt");
        }

        if (movement.getBalanceBefore().compareTo(this.balance) != 0) {
            throw new IllegalStateException("Balance out of sync");
        }

        switch (movement.getMovementType()) {
            case PAYMENT, COMPENSATION -> {
                this.balance = movement.getBalanceAfter();
            }
            case ADJUSTMENT -> {
                this.balance = movement.getBalanceAfter();
            }
        }
    }

    public boolean isPaid() {
        return this.balance.signum() == 0;
    }
}