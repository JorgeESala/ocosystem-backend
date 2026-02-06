package com.ocosur.ocosystem.livechicken.accounting.payment_application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface PaymentApplicationRepository
        extends JpaRepository<PaymentApplication, Long> {

    @Query("""
        SELECT COALESCE(SUM(pa.appliedAmount), 0)
        FROM PaymentApplication pa
        WHERE pa.payment.id = :paymentId
    """)
    BigDecimal sumAppliedAmountByPayment(Long paymentId);
}
