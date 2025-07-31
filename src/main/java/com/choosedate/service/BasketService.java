package com.choosedate.service;

import com.choosedate.domain.dto.PlaceResponseDto;

import java.util.List;


// GPT 보면서 일단 구현하고 해야됨
// Step 3: Service 작성 중에 있음!
public interface BasketService {
    void addToBasket(Long placeId);
    void removeFromBasket(Long placeId);
    List<PlaceResponseDto> getBasketItems();
}
