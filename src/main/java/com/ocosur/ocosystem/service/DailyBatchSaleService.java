package com.ocosur.ocosystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.model.DailyBatchSale;
import com.ocosur.ocosystem.repository.DailyBatchSaleRepository;

@Service
public class DailyBatchSaleService {
    @Autowired
    DailyBatchSaleRepository dailyBatchSaleRepository;
    
    public List<DailyBatchSale> getDailyBatchSale(){
        return dailyBatchSaleRepository.findAll();
    }
}
