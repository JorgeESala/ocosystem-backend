package com.ocosur.ocosystem.livechicken.batchsale;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InboundBatchSaleRepository extends JpaRepository<InboundBatchSale, Long> {
    public Page<InboundBatchSale> findAllByOrderByDateDesc(Pageable pageable);
    public List<InboundBatchSale> findAllByBatch_IdOrderByDateDesc(Long inboundBatchId);
}
