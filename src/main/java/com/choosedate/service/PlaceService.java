package com.choosedate.service;

import com.choosedate.domain.dto.PlaceResponseDto;

import java.util.List;

public interface PlaceService {
    List<PlaceResponseDto> getAllPlaces();
}
