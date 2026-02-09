package com.ocosur.ocosystem.livechicken.accounting.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.employee.service.EmployeeService;
import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;
import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntityService;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.AccountsPayableService;
import com.ocosur.ocosystem.livechicken.accounting.payment.dto.CreateFifoPaymentRequestDTO;
import com.ocosur.ocosystem.livechicken.accounting.payment.dto.CreatePaymentRequestDTO;
import com.ocosur.ocosystem.livechicken.accounting.payment_application.PaymentApplicationService;
import com.ocosur.ocosystem.livechicken.route.Route;
import com.ocosur.ocosystem.livechicken.route.RouteService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentService {

        private final PaymentRepository paymentRepository;
        private final AccountsPayableService accountsPayableService;
        private final PaymentApplicationService paymentApplicationService;
        private final AccountingEntityService accountingEntityService;
        private final EmployeeService employeeService;
        private final RouteService routeService;

        // ------------------------------------------------------------------
        // Creation
        // ------------------------------------------------------------------

        /**
         * Create a payment WITHOUT applying it to any debt.
         * Useful when the application is manual or deferred.
         */
        @Transactional
        public Payment createPayment(
                        Long payerId,
                        Long receiverId,
                        BigDecimal amount,
                        LocalDate paymentDate,
                        PaymentMethod method,
                        String folio,
                        String notes) {
                validateAmount(amount);
                AccountingEntity payer = accountingEntityService.getById(payerId);
                AccountingEntity receiver = accountingEntityService.getById(receiverId);
                Payment payment = Payment.create(
                                payer,
                                receiver,
                                amount,
                                paymentDate,
                                method,
                                folio,
                                null,
                                null,
                                notes);

                return paymentRepository.save(payment);
        }

        /**
         * Create a payment AND apply it using FIFO
         * between a debtor and a creditor.
         */
        @Transactional
        public Payment createAndApplyPaymentFifo(
                        CreateFifoPaymentRequestDTO request) {
                validateAmount(request.getAmount());
                AccountingEntity payer = accountingEntityService.getById(
                                request.getPayerId());

                AccountingEntity receiver = accountingEntityService.getById(
                                request.getReceiverId());
                // 1️⃣ Create payment
                Payment payment = Payment.create(
                                payer,
                                receiver,
                                request.getAmount(),
                                request.getPaymentDate(),
                                request.getPaymentMethod(),
                                request.getFolio(),
                                null,
                                null,
                                request.getNotes());

                paymentRepository.save(payment);

                // 2️⃣ Apply FIFO
                accountsPayableService.applyPaymentFifo(
                                payment,
                                payer,
                                receiver,
                                request.getNotes());

                return payment;
        }

        /**
         * Create a payment AND apply it to a specific accounts payable.
         * Used for manual or exceptional cases.
         */
        @Transactional
        public Payment createAndApplyToSpecificDebt(
                        CreatePaymentRequestDTO request) {
                validateAmount(request.getAmount());
                AccountingEntity payer = accountingEntityService.getById(
                                request.getPayerId());

                AccountingEntity receiver = accountingEntityService.getById(
                                request.getReceiverId());

                Employee driver = request.getDriverId() == null
                                ? null
                                : employeeService.getById(request.getDriverId());

                Route route = request.getRouteId() == null
                                ? null
                                : routeService.getById(request.getRouteId());

                Payment payment = Payment.create(
                                payer,
                                receiver,
                                request.getAmount(),
                                request.getPaymentDate(),
                                request.getPaymentMethod(),
                                request.getFolio(),
                                driver,
                                route,
                                request.getNotes());

                paymentRepository.save(payment);

                paymentApplicationService.applyPayment(
                                payment,
                                accountsPayableService
                                                .getById(request.getAccountsPaymentId()),
                                request.getAmount(),
                                request.getNotes());

                return payment;
        }

        // ------------------------------------------------------------------
        // Validation
        // ------------------------------------------------------------------

        private void validateAmount(BigDecimal amount) {
                if (amount == null || amount.signum() <= 0) {
                        throw new IllegalArgumentException(
                                        "Payment amount must be greater than zero");
                }
        }
}
