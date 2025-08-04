import { BrowserRouter, Routes, Route } from "react-router-dom";
import Register from "../pages/Register";
import Login from "../pages/Login";
import PlaceList from "../pages/PlaceList";
import BasketPage from "../pages/BasketPage";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/places" element={<PlaceList />} />
                <Route path="/basket" element={<BasketPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;