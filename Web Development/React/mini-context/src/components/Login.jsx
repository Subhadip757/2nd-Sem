import React, { useContext, useState } from "react";
import UserContext from "../Context/userContext"

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const {setUser} = useContext(UserContext);

  const handleSubmit = (e) => {
    e.preventDefault();
    setUser({username, password})
  };
  return (
    <div>
      <h2>Login</h2>
      <input
        className="border"
        type="text"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />

      <h2>Password</h2>
      <input
      className="border"
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />

      <button className="border" onClick={handleSubmit}>Submit</button>
    </div>
  );
}

export default Login;
