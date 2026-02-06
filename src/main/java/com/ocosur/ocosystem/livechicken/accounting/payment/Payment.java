package com.ocosur.ocosystem.livechicken.accounting.payment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;
import com.ocosur.ocosystem.livechicken.route.Route;

@Entity
@Table(
    schema = "live_chicken",
    indexes = {
        @Index(
            name = "idx_payment_payer",
            columnList = "payer_entity_id"
        ),
        @Index(
            name = "idx_payment_receiver",
            columnList = "receiver_entity_id"
        ),
        @Index(
            name = "idx_payment_date",
            columnList = "payment_date"
        ),
        @Index(
            name = "idx_payment_folio",
            columnList = "folio"
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -----------------------
    // Accounting entities
    // -----------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payer_entity_id", nullable = false)
    private AccountingEntity payer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_entity_id", nullable = false)
    private AccountingEntity receiver;

    // -----------------------
    // Payment data
    // -----------------------

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 50)
    private PaymentMethod paymentMethod;

    @Column(name = "folio", length = 50, nullable = true)
    private String folio;

    // -----------------------
    // Optional logistics data
    // -----------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "driver_id", nullable = true)
    private Employee driver;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "route_id", nullable = true)
    private Route route;

    // -----------------------
    // Metadata
    // -----------------------

    @Column(name = "note")
    private String note;

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

    public static Payment create(
            AccountingEntity payer,
            AccountingEntity receiver,
            BigDecimal amount,
            LocalDate paymentDate,
            PaymentMethod method,
            String folio,
            Employee driver,
            Route route,
            String note
    ) {
        Payment payment = new Payment();
        payment.payer = payer;
        payment.receiver = receiver;
        payment.amount = amount;
        payment.paymentDate = paymentDate;
        payment.paymentMethod = method;
        payment.folio = folio;
        payment.driver = driver;
        payment.route = route;
        payment.note = note;
        return payment;
    }
}