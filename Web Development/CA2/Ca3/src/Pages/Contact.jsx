import React from 'react';
import { Helmet } from 'react-helmet';

function Contact() {
  return (
    <>
      <Helmet>
        <title>Contact Us - ShopEasy</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
      </Helmet>

      {/* Contact Hero Section */}
      <section className="bg-danger text-white py-5">
        <div className="container">
          <div className="row align-items-center">
            <div className="col-md-6">
              <h1 className="display-4 fw-bold">Get in Touch</h1>
              <p className="lead">We're here to help and answer any questions you might have</p>
            </div>
          </div>
        </div>
      </section>

      {/* Contact Form Section */}
      <section className="py-5">
        <div className="container">
          <div className="row g-4">
            <div className="col-md-6">
              <div className="card border-0 shadow-sm">
                <div className="card-body p-4">
                  <h3 className="mb-4">Send us a Message</h3>
                  <form id="contactForm" onSubmit={(e) => {
                    e.preventDefault();
                    alert('Thank you for your message. We will get back to you soon!');
                    e.target.reset();
                  }}>
                    <div className="mb-3">
                      <label htmlFor="name" className="form-label">Full Name</label>
                      <input type="text" className="form-control" id="name" required />
                    </div>
                    <div className="mb-3">
                      <label htmlFor="email" className="form-label">Email Address</label>
                      <input type="email" className="form-control" id="email" required />
                    </div>
                    <div className="mb-3">
                      <label htmlFor="subject" className="form-label">Subject</label>
                      <input type="text" className="form-control" id="subject" required />
                    </div>
                    <div className="mb-3">
                      <label htmlFor="message" className="form-label">Message</label>
                      <textarea className="form-control" id="message" rows="5" required></textarea>
                    </div>
                    <button type="submit" className="btn btn-danger">Send Message</button>
                  </form>
                </div>
              </div>
            </div>
            <div className="col-md-6">
              <div className="card border-0 shadow-sm h-100">
                <div className="card-body p-4">
                  <h3 className="mb-4">Contact Information</h3>
                  <div className="d-flex mb-4">
                    <i className="fas fa-map-marker-alt text-danger fa-2x me-3"></i>
                    <div>
                      <h5>Address</h5>
                      <p className="mb-0">123 Shopping St, City, Country</p>
                    </div>
                  </div>
                  <div className="d-flex mb-4">
                    <i className="fas fa-phone text-danger fa-2x me-3"></i>
                    <div>
                      <h5>Phone</h5>
                      <p className="mb-0">+1 234 567 8900</p>
                    </div>
                  </div>
                  <div className="d-flex mb-4">
                    <i className="fas fa-envelope text-danger fa-2x me-3"></i>
                    <div>
                      <h5>Email</h5>
                      <p className="mb-0">support@shopeasy.com</p>
                    </div>
                  </div>
                  <div className="mt-4">
                    <h5>Follow Us</h5>
                    <div className="social-links mt-3">
                      <a href="#" className="text-danger me-3"><i className="fab fa-facebook fa-2x"></i></a>
                      <a href="#" className="text-danger me-3"><i className="fab fa-twitter fa-2x"></i></a>
                      <a href="#" className="text-danger me-3"><i className="fab fa-instagram fa-2x"></i></a>
                      <a href="#" className="text-danger me-3"><i className="fab fa-linkedin fa-2x"></i></a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Map Section */}
      <section className="py-5 bg-light">
        <div className="container">
          <div className="row">
            <div className="col-12">
              <h3 className="text-center mb-4">Find Us on Map</h3>
              <div className="ratio ratio-21x9">
                <iframe 
                  src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3152.332792000593!2d-122.41941708468204!3d37.77492997975903!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x8085809c6c8f4459%3A0xb10ed6d9b5050fa5!2sTwitter+HQ!5e0!3m2!1sen!2sus!4v1562619915071!5m2!1sen!2sus" 
                  style={{ border: 0 }} 
                  allowFullScreen 
                  title="Google Maps Location"
                ></iframe>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}

export default Contact;