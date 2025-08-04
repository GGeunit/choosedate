import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "../api/axios"; // axios 인스턴스

function Register() {
    const [form, setForm] = useState({ username: "", email: "", password: "" });
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault(); // 브라우저의 기본 동작(폼 제출)을 막고 개발자가 직접 행동을 컨트롤
        try {
            const res = await axios.post("/users", form);
            setMessage("회원가입 성공! 로그인 페이지로 이동합니다.");
            // 1초 후에 로그인 페이지 이동
            setTimeout(() => {
                navigate("/login");
            }, 1000);
            console.log(res.date);
        } catch (err) {
            setMessage("회원가입 실패");
            console.error(err);
        }
    };

    return (
        <div style={{ maxWidth: "400px", margin: "2rem auto" }}>
            <h2>회원가입</h2>
            <form onSubmit={handleSubmit}>
                <input name="username" placeholder="아이디" onChange={handleChange} /><br />
                <input name="email" placeholder="이메일" onChange={handleChange} /><br />
                <input name="password" type="password" placeholder="비밀번호" onChange={handleChange} /><br />
                <button type="submit">가입하기</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
}

export default Register;