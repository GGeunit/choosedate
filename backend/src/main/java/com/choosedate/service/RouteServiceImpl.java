package com.choosedate.service;

import com.choosedate.domain.dto.RouteSummaryRequestDto;
import com.choosedate.domain.dto.RouteSummaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    @Value("${kakao.mobility.restKey}")
    private String kakaoRestKey;

    private static final double WALK_SPEED_MPS = 4.5 * 1000 / 3600.0; // 4.5 km/h (m/s 변환)

    private RestTemplate rest = new RestTemplate(); // 외부 API(카카오 모빌리티)를 호출하기 위해 사용

    @Override
    public RouteSummaryResponseDto summarize(RouteSummaryRequestDto req) {
        var pts = req.getPoints();
        if(pts == null || pts.size() < 2) {
            throw new IllegalArgumentException("최소 2개 이상의 좌표가 필요합니다.");
        }

        List<RouteSummaryResponseDto.Segment> segs = new ArrayList<>();
        long totalDist = 0, totalDrive = 0, totalWalk = 0;

        for(int i = 0; i < pts.size() - 1; i++) {
            var a = pts.get(i);
            var b = pts.get(i+1);

            // 1) 자동차(택시) 시간/거리: 카카오모빌리티 Directions
            // GET https://apis-navi.kakaomobility.com/v1/directions?origin=lng,lat&destination=lng,lat
            String origin = String.format(Locale.US, "%.6f,%.6f", a.getLongitude(), a.getLatitude());
            String dest = String.format(Locale.US, "%.6f,%.6f", b.getLongitude(), b.getLatitude());

            // 실제 값 확인
            System.out.println("[Kakao] origin=" + origin + " dest=" + dest);

            // URL 조립
            // = String url = "https://apis-navi.kakaomobility.com/v1/directions?origin=" + origin + "&destination=" + dest;
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://apis-navi.kakaomobility.com/v1/directions")
                    .queryParam("origin", origin)
                    .queryParam("destination", dest)
                    .build(true) // true: 인코딩 X(콤마 유지)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoRestKey);

            // HTTP 요청 본문(body) + 헤더를 같이 담는 객체
            // Void: 본문(body)이 없는 요청 → 헤더만 담아서 GET 요청
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            long driveDist = 0L;
            long driveDur = 0L;

            try {
                var resp = rest.exchange(uri, HttpMethod.GET, entity, Map.class); // Map.class: HTTP 응답 본문을 Java의 Map 타입으로 변환해서 받음
                System.out.println("[Kakao] status=" + resp.getStatusCode());
                System.out.println("[Kakao] rawBody=" + resp.getBody());

                // 응답 구조: routes[0].summary.distance, routes[0].summary.duration (ms)
                Map body = resp.getBody();
                if(body != null) {
                    var routes = (List<Map<String, Object>>) body.get("routes");
                    if(routes != null && !routes.isEmpty()) {
                        var summary = (Map<String, Object>) routes.get(0).get("summary");
                        driveDist = ((Number) summary.get("distance")).longValue(); // meters

                        long durationSec = ((Number) summary.get("duration")).longValue(); // sec
                        driveDur = durationSec; // sec
                    }
                }
            } catch(Exception e) {
                // 실패 시에도 전체 흐름 막지 않고 거리/시간은 0으로 둠

                e.printStackTrace();
            }

            // 2) 도보 시간: 하버사인 거리로 추정
            double straight = haversineMeters(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
            long walkDur = Math.round(straight / WALK_SPEED_MPS); // 도보 시간(sec)

            long useDist = (driveDist > 0 ? driveDist : Math.round(straight)); // 사용 거리(자동차 응답 있으면 우선)

            segs.add(RouteSummaryResponseDto.Segment.builder()
                    .indexFrom(i)
                    .indexTo(i+1)
                    .distanceMeters(useDist)
                    .driveDurationSec(driveDur)
                    .walkDurationSec(walkDur)
                    .build());

            totalDist += useDist;
            totalDrive += driveDur;
            totalWalk += walkDur;
        }

        return RouteSummaryResponseDto.builder()
                .segment(segs)
                .total(RouteSummaryResponseDto.Total.builder()
                        .distanceMeters(totalDist)
                        .driveDurationSec(totalDrive)
                        .walkDurationSec(totalWalk)
                        .build())
                .build();
    }

    private static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        // 하버사인(Haversine) 공식: 두 좌표(lat/lon) 사이의 최단거리를 구할 때 지구 반지름을 곱해 실제 거리를 계산
        final double R = 6371_000; // 지구 반지름(meters)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double s1 = Math.sin(dLat/2), s2 = Math.sin(dLon/2);
        double a = s1*s1 + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*s2*s2;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
