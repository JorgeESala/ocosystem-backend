package com.ocosur.ocosystem.core.cedis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CedisResponseDTO {
    private Long id;
    private String name;
}
