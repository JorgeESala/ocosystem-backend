package com.ocosur.ocosystem.livechicken.chickenloss;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(schema = "live_chicken")
public class ChickenLoss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private BigDecimal weight;
    private BigDecimal lossAmount;
    @ManyToOne
    @JoinColumn(name = "inbound_batch_id")
    private InboundBatch batch;
    private LocalDate date;
}
