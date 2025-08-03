package com.choosedate.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 코스 제목 변경
public class CourseUpdateRequestDto {
    private String title; // 사용자가 변경할 새로운 title 이름(기존 title X)
    private List<CoursePlaceUpdateDto> places;
}