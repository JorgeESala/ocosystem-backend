package com.ocosur.ocosystem.livechicken.route;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.livechicken.route.dto.RouteCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.route.dto.RouteResponseDTO;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/live-chicken/routes")
@AllArgsConstructor
public class RouteController {
    
    private final RouteService routeService;

    @GetMapping()
    public List<RouteResponseDTO> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    @PostMapping()
    public RouteResponseDTO createRoute(@RequestBody RouteCreateRequestDTO dto) {
        return routeService.createRoute(dto);
    }

}
