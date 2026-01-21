package com.ocosur.ocosystem.security.mapper;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.security.dto.UserSessionDTO;

public class UserSessionMapper {
    public UserSessionDTO toUserSession(Employee e) {
        return UserSessionDTO.builder()
                .id(e.getId())
                .email(e.getEmail())
                .name(e.getName())
                .allowedBusinesses(
                        e.getBusinesses()
                                .stream()
                                .map(eb -> eb.getBusinessType().getCode())
                                .toList())
                .build();
    }

}
