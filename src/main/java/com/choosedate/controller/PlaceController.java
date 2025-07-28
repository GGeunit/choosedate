package com.choosedate.controller;

import com.choosedate.domain.dto.PlaceResponseDto;
import com.choosedate.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/region")
    public ResponseEntity<List<PlaceResponseDto>> getPlacesByRegion(@RequestParam String region) {
        return ResponseEntity.ok(placeService.getPlacesByRegion(region));
    }
}
