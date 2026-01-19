package com.ocosur.ocosystem.processed.mapper;

import org.springframework.stereotype.Component;

import com.ocosur.ocosystem.processed.client.Client;
import com.ocosur.ocosystem.processed.dto.ClientResponseDTO;

@Component
public class ClientMapper {
    public ClientResponseDTO toResponse(Client client) {
        return ClientResponseDTO.builder()
                .name(client.getName())
                .id(client.getId())
                .build();
    }

}
