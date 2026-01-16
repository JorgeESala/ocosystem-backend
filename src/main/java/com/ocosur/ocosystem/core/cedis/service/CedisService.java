package com.ocosur.ocosystem.core.cedis.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.core.cedis.dto.CedisResponseDTO;
import com.ocosur.ocosystem.core.cedis.repository.CedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CedisService {
    private final CedisRepository cedisRepository;

    public List<CedisResponseDTO> getCedis() {
        return cedisRepository.findAll().stream()
                .map(cedis -> CedisResponseDTO.builder()
                        .id(cedis.getId())
                        .name(cedis.getName())
                        .build())
                        .toList();
    }

}
