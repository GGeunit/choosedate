package com.choosedate.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePreviewResponseDto {
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}
