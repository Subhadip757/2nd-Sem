import React from 'react';
import { Helmet } from 'react-helmet';
import { Link } from 'react-router-dom';
import Footer from '../Components/Footer';
import Header from '../Components/Header';

function About() {
  return (
    <>
      <Helmet>
        <title>About Us - ShopEasy</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
        <style>
          {`
            .about-hero {
              background-color: #dc3545;
              color: white;
              padding: 3rem 0;
            }
            .values-card {
              transition: transform 0.3s ease;
            }
            .values-card:hover {
              transform: translateY(-5px);
            }
          `}
        </style>
      </Helmet>

      {/* Header */}
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
                  <Link className="nav-link active" to="/about">About</Link>
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
                  <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger cart-count">0</span>
                </Link>
              </div>
            </div>
          </div>
        </nav>
      </header>

      {/* About Hero Section */}
      <section className="about-hero">
        <div className="container">
          <div className="row align-items-center">
            <div className="col-md-6">
              <h1 className="display-4 fw-bold">Our Story</h1>
              <p className="lead">Revolutionizing Online Shopping Since 2020</p>
            </div>
          </div>
        </div>
      </section>

      {/* Main Content */}
      <section className="py-5">
        <div className="container">
          <div className="row g-4">
            <div className="col-md-6">
              <h2 className="mb-4">Who We Are</h2>
              <p>ShopEasy is a leading e-commerce platform dedicated to providing customers with a seamless shopping experience. Founded in 2020, we've grown from a small startup to a trusted marketplace serving thousands of satisfied customers.</p>
              <p>Our mission is to make online shopping accessible, convenient, and enjoyable for everyone. We carefully curate our product selection to ensure quality and value for our customers.</p>
            </div>
            <div className="col-md-6">
              <img 
                src="https://images.unsplash.com/photo-1542744173-8e7e53415bb0" 
                alt="Team Meeting" 
                className="img-fluid rounded shadow"
              />
            </div>
          </div>

          {/* Values Section */}
          <div className="row mt-5 g-4">
            <h2 className="text-center mb-4">Our Values</h2>
            <div className="col-md-4">
              <div className="card h-100 border-0 shadow-sm values-card">
                <div className="card-body text-center">
                  <i className="fas fa-heart text-danger fa-3x mb-3"></i>
                  <h4>Customer First</h4>
                  <p>We prioritize customer satisfaction in everything we do.</p>
                </div>
              </div>
            </div>
            <div className="col-md-4">
              <div className="card h-100 border-0 shadow-sm values-card">
                <div className="card-body text-center">
                  <i className="fas fa-shield-alt text-danger fa-3x mb-3"></i>
                  <h4>Quality Assured</h4>
                  <p>We guarantee the quality of every product we sell.</p>
                </div>
              </div>
            </div>
            <div className="col-md-4">
              <div className="card h-100 border-0 shadow-sm values-card">
                <div className="card-body text-center">
                  <i className="fas fa-sync text-danger fa-3x mb-3"></i>
                  <h4>Innovation</h4>
                  <p>We constantly evolve to meet changing customer needs.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
      <Footer />
    </>
  );
}

export default About;