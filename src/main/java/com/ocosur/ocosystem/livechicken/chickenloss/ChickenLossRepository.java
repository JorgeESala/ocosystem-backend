package com.ocosur.ocosystem.livechicken.chickenloss;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChickenLossRepository extends JpaRepository<ChickenLoss, Long> {
    List<ChickenLoss> findAllByBatch_IdOrderByDateDesc(Long batchId);

}
