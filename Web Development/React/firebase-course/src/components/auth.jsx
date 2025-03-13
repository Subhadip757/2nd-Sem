import { useState } from "react";
import { auth, googleProvider } from "../config/firebase";
import {
  createUserWithEmailAndPassword,
  signInWithPopup,
  signOut,
} from "firebase/auth";

export const Auth = () => {
  const [email, setEmail] = useState("");
  const [pass, setPass] = useState("");
  const [message, setMessage] = useState("");

  const signin = async () => {
    if (!email || !pass) {
      setMessage("Email and Password are required!");
      return;
    }

    try {
      await createUserWithEmailAndPassword(auth, email, pass);
      setMessage("User registered successfully!");
    } catch (error) {
      setMessage(error.message);
    }
  };

  const signinWithGoogle = async () => {
    try {
      await signInWithPopup(auth, googleProvider);
      setMessage("User registered successfully!");
    } catch (error) {
      setMessage(error.message);
    }
  };

  const logout = async () => {
    try {
      await signOut(auth);
      setMessage("User logout successfully!");
    } catch (error) {
      setMessage(error.message);
    }
  };

  return (
    <div className="flex flex-col items-center p-5">
      <input
        className="border p-2 mb-3 w-64"
        placeholder="Email.."
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <input
        className="border p-2 mb-3 w-64"
        placeholder="Password..."
        type="password"
        value={pass}
        onChange={(e) => setPass(e.target.value)}
      />
      <button
        onClick={signin}
        type="submit"
        className="border p-2 bg-blue-500 text-white w-64"
      >
        Sign Up
      </button>

      <button onClick={signinWithGoogle}>Sign in with google</button>

      <button onClick={logout}>Log out</button>

      {message && <p className="mt-3 text-red-500">{message}</p>}
    </div>
  );
};
