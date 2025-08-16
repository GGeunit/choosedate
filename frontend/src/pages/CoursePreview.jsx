import { useEffect, useState, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "../api/axios";

function CoursePreview() {
    const location = useLocation();
    const navigate = useNavigate();

    // 선택된 장소 ID
    const [course, setCourse] = useState([]);
    
    // 장소 상세 정보(id → place)
    const [placesMap, setPlacesMap] = useState({});

    // 경로 요약(백엔드 계산: 차량/도보 시간 + 거리)
    const [routeSummary, setRouteSummary] = useState(null);
    
    // 이동 수단
    const [mode, setMode] = useState("drive"); // "drive" | "walk"

    const selectedPlaceIds = location.state?.selectedPlaceIds || [];

    // React에서 특정 DOM 요소(ex. 지도 div)를 직접 참조하기 위함
    // 외부 라이브러리는 직접 DOM 요소를 넘겨줘야 지도 객체 생성 가능
    // React는 Virtual DOM(가상 DOM)을 사용해서 화면을 효율적으로 업데이트 함
    const mapRef = useRef(null); // 지도 div 참조

    // 1) 전체 장소 불러오기 → placesMap 구성 + 선택 ID 적용
    useEffect(() => {

        axios.get("/places")
            .then(res => {
                const map = {};
                res.data.forEach(place => {
                    map[place.id] = place;
                });
                setPlacesMap(map);
                setCourse(selectedPlaceIds); // 순서 저장
            })
            .catch(console.error);
    }, []);

    // 2) 코스/placesMap이 준비되면 백엔드에 경로 요약 요청
    useEffect(() => {
        if(course.length < 2 || Object.keys(placesMap).length === 0) {
            return;
        }

        const points = course.map((id) => {
            const p = placesMap[id];
            return {
                latitude: p.latitude,
                longitude: p.longitude
            };
        });

        axios
            // { points }가 아닌, points로 보내면 body = { [ ... ] }로 전송되어 points라는 key를 찾을 수 없음
            .post("/route/summary", { points }) // body = { points: [ ... ] }
            .then((res) => {
                const raw = res.data ?? {};
                console.log("[/route/summary RAW]", raw);

                // 백엔드가 segment를 안 주고나 null을 줄 가능성 대비
                const segment = Array.isArray(raw.segment) ? raw.segment : []; // 백엔드 segment 키
                const total = raw.total;
                setRouteSummary({ segment, total });
            })
            .catch((err) => console.error("경로 요약 실패", err));
    }, [course, placesMap]);

    // 3) KaKao Map 지도 생성/갱신(마운트 시 지도 생성 + 마커 찍기 + 코스, 장소, 모드가 바뀔 때마다 선 스타일 갱신)
    useEffect(() => {
        if (course.length === 0 || Object.keys(placesMap).length === 0) {
            return;
        }
        // 지도를 그릴 HTML 요소가 없으면 멈출 것
        if (!mapRef.current) {
            return;
        }

        const init = () => {
            const kakao = window.kakao; // index.html에서 설정하여 카카오 지도 API 사용 가능 
            const container = mapRef.current;

            const map = new kakao.maps.Map(container, {
                center: new kakao.maps.LatLng(37.5665, 126.9780), // 초기값: 서울 시청
                level: 5, // 확대 축소 정도(낮을 수록 확대)
            });
            const bounds = new kakao.maps.LatLngBounds(); // 모든 마커를 포함하도록 지도 범위 자동 조정(확대/축소)
            const linePath = []; // 경로를 그릴 때 필요한 좌표 배열

            course.forEach((placeId, idx) => {
                const place = placesMap[placeId];
                const latlng = new kakao.maps.LatLng(place.latitude, place.longitude); // 각각의 장소 좌표 생성

                bounds.extend(latlng); // 범위를 자동으로 조정하기 위해 마커 등록
                linePath.push(latlng); // 경로를 그리기 위해 좌표 추가

                // 지도에 마커 등록
                const marker = new kakao.maps.Marker({
                    map,
                    position: latlng,
                });

                // 장소 이름과 순서를 HTML 형태로 넣은 정보창 객체
                const info = new kakao.maps.InfoWindow({
                    content: `<div style="padding:5px; color:#000; font-size:12px;">
                        ${idx + 1}. ${place.name}
                        </div>`,
                });

                // 마커에 마우스를 올리면 정보창이 열림 / 마커에 마우스를 떼면 정보창 닫힘
                kakao.maps.event.addListener(marker, 'mouseover', () => info.open(map, marker));
                kakao.maps.event.addListener(marker, 'mouseout', () => info.close());
            });

            // 경로 선: 모드에 따라 스타일 변경
            const polyline = new kakao.maps.Polyline({
                path: linePath, // 경로
                strokeWeight: 4, // 굵기
                strokeColor: mode === "drive" ? "#007aff" : "#444", // 색상(차량=파란색, 도보=회색)
                strokeOpacity: 0.9, // 투명도
                strokeStyle: mode === "drive" ? "solid" : "shortdash", // 스타일(차량=실선, 도보=점선)
            });
            polyline.setMap(map);

            map.setBounds(bounds);
        };

        // SDK가 이미 준비됐으면 바로, 아니면 로드 이벤트에 연결
        if (window.kakao && window.kakao.maps) {
            window.kakao.maps.load(init);
        } else {
            const scriptEl = document.querySelector('script[src*="dapi.kakao.com"]');
            if (!scriptEl) {
                console.error("Kakao SDK script not found");
                return;
            }
            const onLoad = () => window.kakao.maps.load(init);
            scriptEl.addEventListener('load', onLoad);
            return () => scriptEl.removeEventListener('load', onLoad);
        }
    }, [course, placesMap, mode]); // course나 placesMap 또는 mode가 바뀔 때마다 지도를 다시 그림

    const moveUp = (index) => {
        if (index === 0) {
            return;
        }
        const newCourse = [...course];
        [newCourse[index - 1], newCourse[index]] = [newCourse[index], newCourse[index - 1]];
        setCourse(newCourse);
    };

    const moveDown = (index) => {
        if (index === course.length - 1) {
            return;
        }
        const newCourse = [...course];
        [newCourse[index + 1], newCourse[index]] = [newCourse[index], newCourse[index + 1]];
        setCourse(newCourse);
    };

    const handleSave = async () => {
        const title = prompt("코스 제목을 입력하세요: ");
        if (!title) {
            return;
        }

        try {
            const res = await axios.post("/course/save", {
                title,
                placeIds: course,
            });
            console.log('save response', res.data.id);
            alert("코스 저장 완료!");
            navigate(`/courses/${res.data.id}`); // 코스 목록 페이지
        } catch (err) {
            console.error("코스 저장 실패", err);
            alert("코스 저장 중 오류 발생");
        }
    };

    // 거리/시간 표기
    const fmtKm = (meters) => Math.round(meters / 100) / 10; // 100m 단위 반올림 → 소수 1자리 km
    const secToMinCeil = (sec) => {
        if(sec === 0) {
            return 0; // 정말로 이동이 없는 경우는 0분
        }
        return Math.ceil(sec / 60);
    };

    return (
    <div style={{ padding: "2rem" }}>
      <h2>🧭 코스 미리보기</h2>

      {/* 이동수단 토글 (★ 추가) */}
      <div style={{ margin: "8px 0 16px" }}>
        <label>
          <input
            type="radio"
            checked={mode === "drive"}
            onChange={() => setMode("drive")}
          />{" "}
          🚕 차량
        </label>
        <label style={{ marginLeft: 12 }}>
          <input
            type="radio"
            checked={mode === "walk"}
            onChange={() => setMode("walk")}
          />{" "}
          🥾 도보
        </label>
      </div>

      {/* 지도 영역 */}
      <div style={{ marginBottom: "1rem", height: "400px" }}>
        <div ref={mapRef} style={{ width: "100%", height: "100%" }} />
      </div>

      {/* 경로 요약 표 (★ 추가) */}
      {routeSummary && (
        <div
          style={{
            border: "1px solid #eee",
            padding: "12px",
            borderRadius: 8,
            marginBottom: 16,
          }}
        >
          <h4 style={{ marginTop: 0 }}>구간별 이동 시간</h4>
          <ul style={{ listStyle: "none", padding: 0, margin: 0 }}>
            {routeSummary.segment.map((seg) => {
              const from = placesMap[course[seg.indexFrom]];
              const to = placesMap[course[seg.indexTo]];
              const sec = mode === "drive" ? seg.driveDurationSec : seg.walkDurationSec;
              const min = secToMinCeil(sec);
              return (
                <li
                  key={`${seg.indexFrom}-${seg.indexTo}`}
                  style={{ padding: "6px 0" }}
                >
                  {seg.indexFrom + 1} → {seg.indexTo + 1} : {from?.name} → {to?.name}
                  {" · "} {min}분 / {fmtKm(seg.distanceMeters)} km
                </li>
              );
            })}
          </ul>

          <div style={{ marginTop: 8, fontWeight: 600 }}>
            총 소요시간:{" "}
            {secToMinCeil(
              mode === "drive"
                ? routeSummary.total.driveDurationSec
                : routeSummary.total.walkDurationSec
            )}
            분{" · "} 총 거리: {fmtKm(routeSummary.total.distanceMeters)} km
          </div>
        </div>
      )}

      <ul style={{ listStyle: "none", padding: 0 }}>
        {course.map((placeId, index) => {
          const place = placesMap[placeId];
          if (!place) return null;
          return (
            <li
              key={placeId}
              style={{ border: "1px solid #ccc", padding: "1rem", marginBottom: "1rem" }}
            >
              <div style={{ display: "flex", justifyContent: "space-between" }}>
                <div>
                  <strong>
                    {index + 1}. {place.name}
                  </strong>
                  <br />
                  <small>
                    {place.region} / {place.category}
                  </small>
                </div>
                <div>
                  <button onClick={() => moveUp(index)}>🔼</button>
                  <button onClick={() => moveDown(index)}>🔽</button>
                </div>
              </div>
            </li>
          );
        })}
      </ul>

      <button onClick={handleSave}>💾 코스 저장</button>
    </div>
  );
}

export default CoursePreview;