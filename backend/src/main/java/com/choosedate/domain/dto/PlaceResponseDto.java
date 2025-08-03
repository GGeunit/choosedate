package com.choosedate.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponseDto {

    private Long id;
    private String name;
    private String category;
    private String keyword;
    private String region;
    private String address;
    private Double latitude;
    private Double longitude;
    private String description;
    private String imageUrl;
}
