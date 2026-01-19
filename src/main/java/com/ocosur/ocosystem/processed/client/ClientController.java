package com.ocosur.ocosystem.processed.client;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.processed.dto.ClientCreateRequestDTO;
import com.ocosur.ocosystem.processed.dto.ClientResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController("processedClientController")
@RequestMapping("/api/processed/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public List<ClientResponseDTO> getAll() {
        return clientService.getAll();
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody ClientCreateRequestDTO client) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clientService.create(client));
    }

}
