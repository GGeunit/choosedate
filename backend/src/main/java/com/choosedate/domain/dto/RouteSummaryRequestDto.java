package com.choosedate.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 코스 이동 거리/시간 계산 request
public class RouteSummaryRequestDto {

    private List<PointDto> points; // 코스 좌표 List

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointDto {
        private double latitude;
        private double longitude;
    }
}
