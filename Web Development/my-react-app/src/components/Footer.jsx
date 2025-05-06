import React from "react";

function Footer() {
  return (
    <footer className="glass-morphism mt-5">
      <div className="container py-5">
        <div className="row g-4">
          <div className="col-md-4">
            <h3 className="h5 mb-3 text-white">Echo Sphere</h3>
            <p className="text-white-50">
              Your gateway to global radio stations. Discover, listen, and enjoy music from around the world.
            </p>
          </div>
          <div className="col-md-4">
            <h3 className="h5 mb-3 text-white">Quick Links</h3>
            <ul className="list-unstyled">
              <li className="mb-2">
                <a href="#about" className="text-decoration-none text-white-50 hover-effect">
                  <i className="fas fa-info-circle me-2"></i>About Us
                </a>
              </li>
              <li className="mb-2">
                <a href="#contact" className="text-decoration-none text-white-50 hover-effect">
                  <i className="fas fa-envelope me-2"></i>Contact
                </a>
              </li>
              <li className="mb-2">
                <a href="#privacy" className="text-decoration-none text-white-50 hover-effect">
                  <i className="fas fa-shield-alt me-2"></i>Privacy Policy
                </a>
              </li>
              <li className="mb-2">
                <a href="#terms" className="text-decoration-none text-white-50 hover-effect">
                  <i className="fas fa-file-contract me-2"></i>Terms of Service
                </a>
              </li>
            </ul>
          </div>
          <div className="col-md-4">
            <h3 className="h5 mb-3 text-white">Connect With Us</h3>
            <div className="social-links">
              <a href="#" className="text-white-50 me-3 social-icon">
                <i className="fab fa-facebook fa-lg"></i>
              </a>
              <a href="#" className="text-white-50 me-3 social-icon">
                <i className="fab fa-twitter fa-lg"></i>
              </a>
              <a href="#" className="text-white-50 me-3 social-icon">
                <i className="fab fa-instagram fa-lg"></i>
              </a>
              <a href="#" className="text-white-50 social-icon">
                <i className="fab fa-youtube fa-lg"></i>
              </a>
            </div>
          </div>
        </div>
        <hr className="my-4 border-light opacity-25" />
        <div className="text-center text-white-50">
          <p className="mb-0">
            &copy; 2025 Echo Sphere | Made with <i className="fas fa-heart text-danger"></i> for music lovers
          </p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;

