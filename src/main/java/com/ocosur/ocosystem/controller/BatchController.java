package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.service.BatchService;

@RestController
@CrossOrigin
@RequestMapping("/api/batch")
public class BatchController {
    @Autowired
    BatchService batchService;


    @GetMapping()
    public ResponseEntity<List<Batch>> getMethodName() {
        return new ResponseEntity<List<Batch>>(batchService.getBatches(), HttpStatus.OK);
    }
    
}
