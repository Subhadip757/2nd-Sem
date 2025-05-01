import React from 'react';
import { Link } from 'react-router-dom';
import CartCount from './CartCount';

function Header() {
  return (
    <header className="bg-white shadow-sm sticky-top">
      <nav className="navbar navbar-expand-lg">
        <div className="container">
          <Link className="navbar-brand fw-bold" to="/">ShopEasy</Link>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav me-auto">
              <li className="nav-item">
                <Link className="nav-link" to="/">Home</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/products">Products</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/about">About</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/contact">Contact</Link>
              </li>
            </ul>
            <div className="d-flex align-items-center gap-3">
              <Link to="/login" className="text-dark">
                <i className="fa-solid fa-user fs-5"></i>
              </Link>
              <Link to="/cart" className="text-dark position-relative">
                <i className="fa-solid fa-cart-shopping fs-5"></i>
                <CartCount />
              </Link>
            </div>
          </div>
        </div>
      </nav>
    </header>
  );
}

export default Header;
