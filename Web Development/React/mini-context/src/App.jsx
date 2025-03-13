import React from "react";
import Profile from "./components/Profile.jsx";
import Login from "./components/Login.jsx";
import UserContextProvider from "./Context/UserContext.jsx";

function App() {
  return (
    <UserContextProvider>
      <Login />
      <Profile />
    </UserContextProvider>
  );
}

export default App;
