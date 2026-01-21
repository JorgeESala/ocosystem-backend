package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.model.Ticket;
import com.ocosur.ocosystem.security.RequireBusiness;
import com.ocosur.ocosystem.service.TicketService;

import org.springframework.web.bind.annotation.GetMapping;


@CrossOrigin
@RestController
@RequestMapping("/api/tickets")
@RequireBusiness("BRANCHES")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;
    @GetMapping()
    public ResponseEntity<List<Ticket>> getTickets() {
        return new ResponseEntity<List<Ticket>>(ticketService.getTickets(), HttpStatus.OK);
    }
    
}
