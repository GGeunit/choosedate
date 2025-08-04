import { useEffect, useState } from "react";
import axios from "../api/axios";
import { Link } from "react-router-dom";

function PlaceList() {
    const [places, setPlaces] = useState([]);
    const [basket, setBasket] = useState([]);

    // 장바구니 조회
    useEffect(() => {
        axios.get("/basket")
            // .map(item => item.placeId): 배열을 변환해(각 item의 placeId만 꺼내) 새로운 배열을 만듦
            .then(res => setBasket(res.data.map(item => item.placeId)))
            .catch(console.error);
    }, []);

    // useEffect(): 컴포넌트가 처음 렌더링될 때 실행되는 코드
    useEffect(() => {
        axios.get("/places")
            .then(res => {
                setPlaces(res.data);
            })
            .catch(err => {
                console.error("장소 불러오기 실패", err);
            });
    }, []); // []: 의존성 배열(한 번만 실행)

    const toggleBasket = async (placeId) => {
        const isInBasket = basket.includes(placeId); // boolean(placeId가 basket 배열 안에 있는가?)
        try {
            if (isInBasket) { // 장바구니에 이미 존재한다면 삭제
                await axios.delete(`/basket/remove?placeId=${placeId}`);
                // filter: 배열에서 조건을 만족하는 요소만 남기고 새 배열을 만드는 함수
                // placeId와 같은 아이템은 제거됨
                setBasket(basket.filter(id => id !== placeId));
            } else { // 장바구니에 존재하지 않으면 추가
                await axios.post(`basket/add?placeId=${placeId}`);
                setBasket([...basket, placeId]);
            }
        } catch (err) {
            console.error("장바구니 처리 실패", err);
        }
    };

    return (
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
            <h2>📍 장소 목록</h2>
            <Link to="/basket">
                <button>🧺 장바구니 보기</button>
            </Link>
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(250px, 1fr))", gap: "1rem" }}>
                {places.map(place => {
                    const isInBasket = basket.includes(place.id);
                    return (
                        <div key={place.id} style={{ border: "1px solid #ccc", padding: "1rem", borderRadius: "8px" }}>
                            <h3>{place.name}</h3>
                            <p>{place.description}</p>
                            <p><b>지역:</b> {place.region}</p>
                            <p><b>카테고리:</b> {place.category}</p>
                            <button onClick={() => toggleBasket(place.id)}>
                                {isInBasket ? "❌ 장바구니 삭제" : "🛒 담기"}
                            </button>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}

export default PlaceList;