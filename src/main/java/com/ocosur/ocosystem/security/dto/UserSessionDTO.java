package com.ocosur.ocosystem.security.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSessionDTO {

    private Long id;
    private String email;
    private String name;
    private List<String> allowedBusinesses;
}
