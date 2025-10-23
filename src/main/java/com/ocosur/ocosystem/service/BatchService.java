package com.ocosur.ocosystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.repository.BatchRepository;

@Service
public class BatchService {
    @Autowired
    private BatchRepository batchRepository;
    public List<Batch> getBatches(){
        return batchRepository.findAll();
    }
}
