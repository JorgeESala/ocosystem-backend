package com.ocosur.ocosystem.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.dto.BatchSaleDTO;
import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.BatchSale;
import com.ocosur.ocosystem.repository.BatchSaleRepository;

@Service
public class BatchSaleService {
    @Autowired
    BatchSaleRepository batchSaleRepository;
    
    public List<BatchSale> getBatchSales(){
        return batchSaleRepository.findAll();
    }
    public List<BatchSale> getBatchSalesByBatchId(Integer batchId){
        return batchSaleRepository.findByBatchId(batchId);
    }
    public BatchSale createBatchSale(BatchSaleDTO request) {
        Batch batch = new Batch();
        batch.setId(request.getBatchId());
        
        BatchSale batchSale = new BatchSale();
        batchSale.setBatch(batch);
        batchSale.setQuantitySold(request.getQuantitySold());
        batchSale.setDate(request.getDate());
        batchSale.setKgTotal(request.getKgTotal());
        batchSale.setSaleTotal(request.getSaleTotal());
        batchSale.setKgGut(request.getKgGut());
        return batchSaleRepository.save(batchSale);
    }
    public BatchSale updateBatchSale(BatchSale batchSale){
        return batchSaleRepository.save(batchSale);
    }
}
