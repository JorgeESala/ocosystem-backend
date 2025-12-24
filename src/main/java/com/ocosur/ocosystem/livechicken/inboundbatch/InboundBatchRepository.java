package com.ocosur.ocosystem.livechicken.inboundbatch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundBatchRepository extends JpaRepository<InboundBatch, Long> {
    Page<InboundBatch> findAllByOrderByDateDesc(Pageable pageable);
}
