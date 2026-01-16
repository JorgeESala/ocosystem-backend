package com.ocosur.ocosystem.core.cedis.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.core.cedis.dto.CedisResponseDTO;
import com.ocosur.ocosystem.core.cedis.service.CedisService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cedis")
@RequiredArgsConstructor
public class CedisController {
    private final CedisService cedisService;

    @GetMapping()
    public List<CedisResponseDTO> getCedis(){
        return cedisService.getCedis();
    }
}
