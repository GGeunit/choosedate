package com.choosedate.controller;

import com.choosedate.domain.dto.RouteSummaryRequestDto;
import com.choosedate.domain.dto.RouteSummaryResponseDto;
import com.choosedate.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/route")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/summary")
    public ResponseEntity<RouteSummaryResponseDto> summary(@RequestBody RouteSummaryRequestDto req) {
        return ResponseEntity.ok(routeService.summarize(req));
    }
}
