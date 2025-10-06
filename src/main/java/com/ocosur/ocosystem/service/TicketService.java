package com.ocosur.ocosystem.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ocosur.ocosystem.model.Ticket;
import com.ocosur.ocosystem.repository.TicketRepository;

@Service
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;

    

    public List<Ticket> getTickets(){
        return ticketRepository.findAll();
    }
}
