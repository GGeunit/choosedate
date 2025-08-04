import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../api/axios";

function BasketPage() {
    const [places, setPlaces] = useState([]);
    const [selected, setSelected] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get("/basket")
            .then(res => setPlaces(res.data))
            .catch(console.error);
    }, []);

    const handleSelect = (placeId) => {
        if (selected.includes(placeId)) {
            setSelected(selected.filter(id => id !== placeId));
        }
        else {
            setSelected([...selected, placeId]);
        }
    };

    const handlePreview = () => {
        if (selected.length === 0) {
            alert("최소 1개 이상 선택해주세요.");
            return;
        }

        // 선택한 placeId 배열을 preview 페이지로 전달
        navigate("/preview", { state: { selectedPlaceIds: selected } });
    };

    return (
        <div style={{ padding: "2rem" }}>
            <h2>🧺 장바구니</h2>
            {places.length === 0 ? (
                <p>장바구니가 비어 있습니다.</p>
            ) : (
                <ul style={{ listStyle: "none", padding: 0 }}>
                    {places.map(place => (
                        <li key={place.id} style={{ marginBottom: "1rem", border: "1px solid #ccc", padding: "1rem" }}>
                            <label>
                                <input
                                    type="checkbox"
                                    checked={selected.includes(place.id)}
                                    onChange={() => handleSelect(place.id)}
                                />{" "}
                                <strong>{place.name}</strong> - {place.region} / {place.category}
                            </label>
                        </li>
                    ))}
                </ul>
            )}
            <button onClick={handlePreview} disabled={selected.length === 0}>
                🧭 코스 미리보기
            </button>
        </div>
    );
}

export default BasketPage;