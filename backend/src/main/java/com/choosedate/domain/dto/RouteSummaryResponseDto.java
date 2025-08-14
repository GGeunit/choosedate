package com.choosedate.domain.dto;

import lombok.*;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 코스 이동 거리/시간 계산 response
public class RouteSummaryResponseDto {

    private List<Segment> segment; // 구간별 요약
    private Total total; // 총합

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Segment {
        // (RouteSummaryRequestDto의 points의) 출발지 인덱스
        private int indexFrom;

        // (RouteSummaryRequestDto의 points의) 도착지 인덱스
        private int indexTo;

        // 자동차 기준 응답(없으면 하버사인(두 좌표 사이의 최단거리 · 직선거리를 미터로 계산))
        private long distanceMeters;

        private long driveDurationSec; // 카카오모빌리티 자동차

        private long walkDurationSec; // 하버사인 기반 (4.5km/h)
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Total {
        private long distanceMeters;
        private long driveDurationSec;
        private long walkDurationSec;
    }
}