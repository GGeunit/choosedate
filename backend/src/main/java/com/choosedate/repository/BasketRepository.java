package com.choosedate.repository;

import com.choosedate.domain.Basket;
import com.choosedate.domain.Place;
import com.choosedate.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    // 조회 결과가 없으면 null이 아니라 빈 리스트( [] )가 반환되어 안전함
    List<Basket> findByUser(User user);

    // 하나의 엔티티를 찾는 메서드로 Optional로 감싸서 null을 안전하게 처리
    Optional<Basket> findByUserAndPlace(User user, Place place);
}