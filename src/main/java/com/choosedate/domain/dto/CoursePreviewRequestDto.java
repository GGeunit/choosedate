package com.choosedate.domain.dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePreviewRequestDto {
    private List<Long> placeIds; // 장소 ID 리스트
}
