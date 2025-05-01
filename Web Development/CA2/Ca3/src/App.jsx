import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./Pages/Home";
import Products from "./Pages/Products";
import About from "./Pages/About";
import Contact from "./Pages/Contact";
import Cart from "./Pages/Cart";
import Confirmation from "./Pages/Confirmation";
import Login from "./Pages/Login";
import Checkout from "./Pages/Checkout";

export default function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/products" element={<Products />} />
                <Route path="/about" element={<About />} />
                <Route path="/contact" element={<Contact />} />
                <Route path="/cart" element={<Cart />} />
                <Route path="/login" element={<Login />} />
                <Route path="/confirmation" element={<Confirmation />} />
                <Route path="/checkout" element={<Checkout />} />
                {/* Add more routes as needed */}
            </Routes>
        </Router>
    );
}
