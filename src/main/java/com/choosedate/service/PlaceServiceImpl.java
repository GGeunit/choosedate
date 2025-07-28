package com.choosedate.service;

import com.choosedate.domain.Place;
import com.choosedate.domain.dto.PlaceResponseDto;
import com.choosedate.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;

    @Override
    public List<PlaceResponseDto> getAllPlaces() {
        return placeRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private PlaceResponseDto toDto(Place place) {
        return PlaceResponseDto.builder()
                .id(place.getId())
                .name(place.getName())
                .category(place.getCategory())
                .keyword(place.getKeyword())
                .region(place.getRegion())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .description(place.getDescription())
                .imageUrl(place.getImageUrl())
                .build();
    }
}
