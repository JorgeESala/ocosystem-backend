package com.ocosur.ocosystem.livechicken.batchsale;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.livechicken.inboundbatch.InboundBatch;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "live_chicken")
public class InboundBatchSale {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "inbound_batch_id")
    private InboundBatch batch;
    private BigDecimal quantitySold;
    private BigDecimal kgSold;
    private BigDecimal kgSent;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private BigDecimal saleTotal;
    private LocalDate date;
}
