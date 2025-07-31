package com.choosedate.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 연관
    @ManyToOne // 한 명의 User은 여러 Basket을 가질 수 있음(연관관계 필수 지정 필요)
    @JoinColumn(name = "user_id")
    private User user;

    // 장소 연관
    @ManyToOne // 한 개의 place는 여러 Basket을 가질 수 있음(user가 다양하기 때문)
    @JoinColumn(name = "place_id")
    private Place place;
}
