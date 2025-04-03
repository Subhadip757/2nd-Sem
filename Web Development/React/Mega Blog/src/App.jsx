import React, { useEffect, useState } from "react"
import {useDispatch} from "react-redux"
import authService from "./Appwrite/auth.js";
import { login, logout } from "./Store/authSlice.js";
import Header  from "./components/header/Header.jsx";
import Footer from "./components/footer/Footer.jsx";

function App() {
  const [loading, setLoading] = useState(true);
  const dispatch = useDispatch();

  useEffect(() => {
    authService.getCurrentUser()
    .then((userData) => {
      if(userData){
        dispatch(login({userData}));
      }
      else{
        dispatch(logout());
      }
    })
    .finally(() => setLoading(false));
  }, [])
  
  return !loading ? (
    <div className="min-h-screen flex flex-wrap content-betweem bg-gray-400">
      <div className="w-full block">
        <Header />
        <main>
          {/* outlet */}
        </main>
        <Footer />
      </div>
    </div>
  ) : (null)
}

export default App
