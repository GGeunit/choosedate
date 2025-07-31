package com.choosedate.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSaveRequestDto {
    private String title;
    private List<Long> placeIds; // 순서대로 전달
}
