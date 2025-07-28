package com.choosedate.controller;

import com.choosedate.domain.dto.PlaceResponseDto;
import com.choosedate.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<PlaceResponseDto>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }
}
