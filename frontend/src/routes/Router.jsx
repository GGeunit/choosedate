import { BrowserRouter, Routes, Route } from "react-router-dom";
import Register from "../pages/Register";
import Login from "../pages/Login";
import PlaceList from "../pages/PlaceList";
import BasketPage from "../pages/BasketPage";
import CoursePreview from "../pages/CoursePreview";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/places" element={<PlaceList />} />
                <Route path="/basket" element={<BasketPage />} />
                <Route path="/preview" element={<CoursePreview />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;