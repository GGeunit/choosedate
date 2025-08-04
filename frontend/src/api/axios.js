import axios from 'axios'

const instance = axios.create({
    baseURL: 'http://localhost:8080/api', // Spring Boot(백엔드) API 주소
    withCredentials: true, // 쿠키 인증 시 필요
});

instance.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if(token) {
        config.headers.Authorization = `Bearer ${token}`;
        console.log("🔐 토큰 포함됨:");
    }
    else {
        console.warn("❗ 로그인 토큰 없음 (Authorization 헤더 미포함)");
    }
    return config;
});

export default instance;