package com.choosedate.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {
    private Long id;
    private String title;
    private List<PlaceResponseDto> places; // 순서 포함
}
