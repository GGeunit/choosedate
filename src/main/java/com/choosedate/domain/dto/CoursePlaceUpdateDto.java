package com.choosedate.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 코스 장소 순서 변경
public class CoursePlaceUpdateDto {
    private Long placeId;
    private int sequence;
}
