package com.choosedate.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseLocationDto {
    private double latitude;
    private double longitude;
}
