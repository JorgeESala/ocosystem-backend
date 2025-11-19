package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.model.Order;
import com.ocosur.ocosystem.service.OrderService;

@CrossOrigin
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getOrderss(){
    return new ResponseEntity<List<Order>>(orderService.getOrders(), HttpStatus.OK);
    }
}
