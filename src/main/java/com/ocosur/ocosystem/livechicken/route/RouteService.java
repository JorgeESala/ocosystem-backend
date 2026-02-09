package com.ocosur.ocosystem.livechicken.route;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.route.dto.RouteCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.route.dto.RouteResponseDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteService {
    private final RouteRepository routeRepository;

    public List<RouteResponseDTO> getAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        return routes.stream()
                .map(route -> RouteResponseDTO.builder()
                        .id(route.getId())
                        .name(route.getName())
                        .build())
                .toList();
    }

    public RouteResponseDTO createRoute(RouteCreateRequestDTO dto) {
        Route route = new Route();
        route.setName(dto.getName());
        Route savedRoute = routeRepository.save(route);
        return RouteResponseDTO.builder()
                .id(savedRoute.getId())
                .name(savedRoute.getName())
                .build();
    }

    public Route getById(Long routeId) {
        return routeRepository.findById(routeId).orElseThrow(() -> new EntityNotFoundException(
                "Ruta con el id " + routeId + " no encontrada"));
    }
}
