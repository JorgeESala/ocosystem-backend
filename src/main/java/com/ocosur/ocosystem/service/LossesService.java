package com.ocosur.ocosystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.model.Losses;
import com.ocosur.ocosystem.repository.LossesRepository;

@Service
public class LossesService {
    @Autowired
    LossesRepository lossesRepository;
    
    public List<Losses> getLosses(){
        return lossesRepository.findAll();
    }
}
