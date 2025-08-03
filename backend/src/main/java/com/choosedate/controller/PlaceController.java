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

    // 전체 장소 조회
    @GetMapping
    public ResponseEntity<List<PlaceResponseDto>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    // 지역 기반 조회
    @GetMapping("/region")
    public ResponseEntity<List<PlaceResponseDto>> getPlacesByRegion(@RequestParam String region) {
        return ResponseEntity.ok(placeService.getPlacesByRegion(region));
    }

    // 지역 + 카테고리 기반 조회
    @GetMapping("/region-category")
    public ResponseEntity<List<PlaceResponseDto>> getPlacesByRegionAndCategory(@RequestParam String region, @RequestParam String category) {
        return ResponseEntity.ok(placeService.getPlacesByRegionAndCategory(region, category));
    }

    // 지역 + 키워드 기반 조회
    @GetMapping("/region-keyword")
    public ResponseEntity<List<PlaceResponseDto>> getPlacesByRegionAndKeyword(@RequestParam String region, @RequestParam String keyword) {
        return ResponseEntity.ok(placeService.getPlacesByRegionAndKeyword(region, keyword));
    }

}
