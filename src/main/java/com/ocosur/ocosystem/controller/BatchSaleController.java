package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ocosur.ocosystem.dto.BatchSaleDTO;
import com.ocosur.ocosystem.model.BatchSale;
import com.ocosur.ocosystem.service.BatchSaleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@CrossOrigin
@RequestMapping("/api/batchSales")
public class BatchSaleController {
    @Autowired
    BatchSaleService batchSaleService;

    @GetMapping()
    public ResponseEntity<List<BatchSale>> getBatchSales() {
        return new ResponseEntity<List<BatchSale>>(batchSaleService.getBatchSales(), HttpStatus.OK);
    }
    @GetMapping("/{batchId}")
    public ResponseEntity<List<BatchSale>> getBatchSaleByBatchId(@PathVariable Integer batchId) {
        return new ResponseEntity<List<BatchSale>>(batchSaleService.getBatchSalesByBatchId(batchId), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<BatchSale> updateBatchSale(@PathVariable Integer id, @RequestBody BatchSale batchSale) {
        batchSale.setId(id);
        return new ResponseEntity<BatchSale>(batchSaleService.updateBatchSale(batchSale), HttpStatus.OK);
    }
   
    @PostMapping()
    public BatchSale createSale(@RequestBody BatchSaleDTO batchSale){
        return batchSaleService.createBatchSale(batchSale);
    }
}
