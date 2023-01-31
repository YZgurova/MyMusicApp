import {Navigate, Route, Routes} from "react-router-dom"
import  LoginForm  from "./forms/LoginForm"
import RegisterForm from "./forms/RegisterForm"
import  UserHomeProfile  from "./UserHomeProfile"
import CreatorProfile from "./CreatorProfile";
import AdminProfile from "./Admin"

function App() {
    return (
        <div>
            <Routes>
                <Route path = "/" element={<Navigate to="/login" />} />
                <Route path="/register" element={<RegisterForm />} />
                <Route path="/login" element={<LoginForm />} />
                <Route path="/user" element={<UserHomeProfile />} />
                <Route path="/creator" element={<CreatorProfile/>}/>
                <Route path="/admin" element={<AdminProfile/>}/>
            </Routes>
        </div>
    )
}

 export default App;