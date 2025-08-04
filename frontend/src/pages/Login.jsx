import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../api/axios";

function Login() {
    const [form, setForm] = useState({ username: "", password: "" });
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const res = await axios.post("/users/login", form);
            const token = res.data.token;
            if(!token) {
                throw new Error("토큰 없음");
            }

            localStorage.setItem("token", token); // 토큰 저장
            setError("");
            navigate("/places");
        } catch (err) {
            setError("로그인 실패");
            console.error(err);
        }
    };

    return (
        <div style={{ maxWidth: "400px", margin: "2rem auto" }}>
            <h2>로그인</h2>
            <form onSubmit={handleLogin}>
                <input name="username" placeholder="아이디" onChange={handleChange} /><br />
                <input name="password" type="password" placeholder="비밀번호" onChange={handleChange} /><br />
                <button type="submit">로그인</button>
            </form>
            {error && <p style={{ color: "red" }}>{error}</p>}
        </div>
    );


}

export default Login;