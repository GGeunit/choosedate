package com.choosedate.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 장소 이름

    private String category; // 맛집 / 카페 / 놀거리 / 숨은 장소

    private String keyword; // ex) 삼겹살 / 파스타 / 감자탕

    private String region; // 지역명 ex) 서울 강남

    private String address; // 전체 주소

    private Double latitude; // 위도

    private Double longitude; // 경도

    private String description; // 간단한 소개

    private String imageUrl; // 썸네일 이미지 URL
}