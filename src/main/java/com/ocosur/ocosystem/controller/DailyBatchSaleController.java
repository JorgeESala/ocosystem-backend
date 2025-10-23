package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.model.DailyBatchSale;
import com.ocosur.ocosystem.service.DailyBatchSaleService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin
@RequestMapping("/api/dailyBatchSale")
public class DailyBatchSaleController {
    @Autowired
    DailyBatchSaleService dailyBatchSaleService;

    @GetMapping()
    public ResponseEntity<List<DailyBatchSale>> getDailyBatchSale() {
        return new ResponseEntity<List<DailyBatchSale>>(dailyBatchSaleService.getDailyBatchSale(), HttpStatus.OK);
    }
    
    
}
