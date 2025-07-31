package com.choosedate.service;

import com.choosedate.domain.Basket;
import com.choosedate.domain.Place;
import com.choosedate.domain.User;
import com.choosedate.domain.dto.PlaceResponseDto;
import com.choosedate.repository.BasketRepository;
import com.choosedate.repository.PlaceRepository;
import com.choosedate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    private User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getUsername();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
    }

    @Override
    public void addToBasket(Long placeId) {
        User user = getCurrentUser();
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소가 존재하지 않습니다."));

       // 이미 존재하는 항목인지 확인
        Optional<Basket> existing = basketRepository.findByUserAndPlace(user, place);
        if(existing.isEmpty()) {
            Basket basket = Basket.builder()
                    .user(user)
                    .place(place)
                    .build();
            basketRepository.save(basket);
        }
    }

    @Override
    public void removeFromBasket(Long placeId) {
        User user = getCurrentUser();
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소가 존재하지 않습니다."));

        basketRepository.findByUserAndPlace(user, place)
                .ifPresent(basketRepository::delete);
    }

    @Override
    public List<PlaceResponseDto> getBasketItems() {
        User user = getCurrentUser();
        System.out.println("현재 사용자 ID: " + user.getId());

        List<Basket> baskets = basketRepository.findByUser(user);

        return baskets.stream()
                .map(b -> PlaceResponseDto.builder()
                        .id(b.getPlace().getId())
                        .name(b.getPlace().getName())
                        .category(b.getPlace().getCategory())
                        .keyword(b.getPlace().getKeyword())
                        .region(b.getPlace().getRegion())
                        .address(b.getPlace().getAddress())
                        .latitude(b.getPlace().getLatitude())
                        .longitude(b.getPlace().getLongitude())
                        .description(b.getPlace().getDescription())
                        .imageUrl(b.getPlace().getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }
}
