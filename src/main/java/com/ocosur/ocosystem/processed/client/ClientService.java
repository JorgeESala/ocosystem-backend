package com.ocosur.ocosystem.processed.client;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.processed.dto.ClientCreateRequestDTO;
import com.ocosur.ocosystem.processed.dto.ClientResponseDTO;
import com.ocosur.ocosystem.processed.mapper.ClientMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service("processedClientService")
@RequiredArgsConstructor
public class ClientService {
    private final ProcessedClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public List<ClientResponseDTO> getAll() {
        return clientRepository.findAll().stream().map(client -> ClientResponseDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .build()).toList();
    }
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }
    @Transactional
    public ClientResponseDTO create(ClientCreateRequestDTO request) {
        Client client = Client.builder().name(request.getName()).build();
        Client saved = clientRepository.save(client);

        return clientMapper.toResponse(saved);
    }
}
