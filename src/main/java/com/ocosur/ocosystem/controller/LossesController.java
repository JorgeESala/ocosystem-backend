package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.model.Losses;
import com.ocosur.ocosystem.service.LossesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin
@RestController
@RequestMapping("/api/losses")
public class LossesController {
    @Autowired
    LossesService lossesService;

    @GetMapping()
    public ResponseEntity<List<Losses>> getMethodName(@RequestParam String param) {
        return new ResponseEntity<List<Losses>>(lossesService.getLosses(), HttpStatus.OK);
    }
    
    
}
