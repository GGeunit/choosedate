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
// 코스를 구성하는 각각의 장소와 순서(우선순위)
public class CoursePlace { // course_place 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sequence; // 순서(몇 번째 장소인지)

    @ManyToOne
    @JoinColumn(name = "course_id") // default
    private Course course; // course_id

    @ManyToOne
    @JoinColumn(name = "place_id") // default
    private Place place; // place_id
}