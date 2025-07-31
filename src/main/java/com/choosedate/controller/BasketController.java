package com.choosedate.controller;

import com.choosedate.domain.dto.PlaceResponseDto;
import com.choosedate.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping("/add")
    public ResponseEntity<Void> addToBasket(@RequestParam Long placeId) {
        basketService.addToBasket(placeId);

        // return ResponseEntity.ok(); → body 필요(Void X)
        return ResponseEntity.ok().build(); // build(): 아무 것도 반환하지 않음(Void)
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromBasket(@RequestParam Long placeId) {
        basketService.removeFromBasket(placeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PlaceResponseDto>> getBasket() {
        return ResponseEntity.ok(basketService.getBasketItems());
    }
}
