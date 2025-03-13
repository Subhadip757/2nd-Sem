import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider } from "firebase/auth";
import { getFirestore } from "firebase/firestore";

const firebaseConfig = {
  apiKey: "AIzaSyBZsGYdATzZRSudaUG91MB963ZNjl9oeFI",
  authDomain: "fir-project-41abe.firebaseapp.com",
  projectId: "fir-project-41abe",
  storageBucket: "fir-project-41abe.firebasestorage.app",
  messagingSenderId: "120209796245",
  appId: "1:120209796245:web:0cb979184780b2b2327f9c",
  measurementId: "G-9G45XZKHQ5",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const googleProvider = new GoogleAuthProvider();
export const db = getFirestore(app);
