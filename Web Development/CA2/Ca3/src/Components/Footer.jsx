import React from 'react';
import { Link } from 'react-router-dom';

function Footer() {
  return (
    <footer className="bg-dark text-white py-4 mt-auto">
      <div className="container">
        <div className="row">
          <div className="col-md-4">
            <h5>ShopEasy</h5>
            <p>Your one-stop destination for all your shopping needs.</p>
          </div>
          <div className="col-md-4">
            <h5>Quick Links</h5>
            <ul className="list-unstyled">
              <li><Link to="/" className="text-white">Home</Link></li>
              <li><Link to="/products" className="text-white">Products</Link></li>
              <li><Link to="/about" className="text-white">About</Link></li>
              <li><Link to="/contact" className="text-white">Contact</Link></li>
            </ul>
          </div>
          <div className="col-md-4">
            <h5>Contact Us</h5>
            <ul className="list-unstyled">
              <li><i className="fas fa-envelope me-2"></i> support@shopeasy.com</li>
              <li><i className="fas fa-phone me-2"></i> +1 (555) 123-4567</li>
              <li><i className="fas fa-map-marker-alt me-2"></i> 123 Shopping St, Retail City</li>
            </ul>
          </div>
        </div>
        <hr />
        <div className="text-center">
          <p className="mb-0">&copy; {new Date().getFullYear()} ShopEasy. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
