import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Helmet } from "react-helmet";

function Checkout() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    address: "",
    city: "",
    state: "",
    zip: "",
    country: "",
    payment: "",
    cardNumber: "",
  });
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleSubmit(e) {
    e.preventDefault();
    setSubmitting(true);
    setTimeout(() => {
      localStorage.setItem("lastOrder", JSON.stringify(form));
      setSubmitting(false);
      navigate("/confirmation");
    }, 1200);
  }

  return (
    <>
      <Helmet>
        <title>Checkout - ShopEasy</title>
        <style>
          {`
            body {
              background: #f6f7fb !important;
            }
            .checkout-card {
              border-radius: 18px;
              box-shadow: 0 8px 32px rgba(44,62,80,0.10), 0 1.5px 4px rgba(220,53,69,0.08);
              animation: fadeInUp 0.7s;
              background: #fff;
            }
            @keyframes fadeInUp {
              from { opacity: 0; transform: translateY(40px);}
              to { opacity: 1; transform: translateY(0);}
            }
            .form-floating > .form-control:focus, 
            .form-floating > .form-select:focus {
              border-color: #dc3545;
              box-shadow: 0 0 0 0.2rem rgba(220,53,69,.15);
            }
            .form-floating label {
              color: #888;
            }
            .form-floating .input-icon {
              position: absolute;
              left: 1rem;
              top: 50%;
              transform: translateY(-50%);
              color: #dc3545;
              font-size: 1.1em;
              pointer-events: none;
            }
            .form-floating input, .form-floating select {
              padding-left: 2.5rem;
            }
            .checkout-section-title {
              font-size: 1.1rem;
              font-weight: 600;
              color: #dc3545;
              margin-bottom: 1rem;
              letter-spacing: 0.5px;
            }
          `}
        </style>
      </Helmet>
      <div className="bg-light min-vh-100 py-5">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-lg-7">
              <div className="card checkout-card border-0">
                <div className="card-body p-5">
                  <h2 className="mb-4 text-center fw-bold text-danger">
                    Checkout
                  </h2>
                  <form onSubmit={handleSubmit} autoComplete="off">
                    <div className="checkout-section-title">Shipping Information</div>
                    <div className="row g-3 mb-4">
                      <div className="col-md-6">
                        <div className="form-floating position-relative">
                          <input
                            name="firstName"
                            className="form-control"
                            id="firstName"
                            placeholder="First Name"
                            value={form.firstName}
                            onChange={handleChange}
                            required
                          />
                          <span className="input-icon"><i className="fa fa-user"></i></span>
                          <label htmlFor="firstName">First Name</label>
                        </div>
                      </div>
                      <div className="col-md-6">
                        <div className="form-floating position-relative">
                          <input
                            name="lastName"
                            className="form-control"
                            id="lastName"
                            placeholder="Last Name"
                            value={form.lastName}
                            onChange={handleChange}
                            required
                          />
                          <span className="input-icon"><i className="fa fa-user"></i></span>
                          <label htmlFor="lastName">Last Name</label>
                        </div>
                      </div>
                      <div className="col-12">
                        <div className="form-floating position-relative">
                          <input
                            name="address"
                            className="form-control"
                            id="address"
                            placeholder="Address"
                            value={form.address}
                            onChange={handleChange}
                            required
                          />
                          <span className="input-icon"><i className="fa fa-home"></i></span>
                          <label htmlFor="address">Address</label>
                        </div>
                      </div>
                      <div className="col-md-4">
                        <div className="form-floating position-relative">
                          <input
                            name="city"
                            className="form-control"
                            id="city"
                            placeholder="City"
                            value={form.city}
                            onChange={handleChange}
                            required
                          />
                          <span className="input-icon"><i className="fa fa-city"></i></span>
                          <label htmlFor="city">City</label>
                        </div>
                      </div>
                      <div className="col-md-4">
                        <div className="form-floating position-relative">
                          <input
                            name="state"
                            className="form-control"
                            id="state"
                            placeholder="State"
                            value={form.state}
                            onChange={handleChange}
                            required
                          />
                          <span className="input-icon"><i className="fa fa-flag"></i></span>
                          <label htmlFor="state">State</label>
                        </div>
                      </div>
                      <div className="col-md-4">
                        <div className="form-floating position-relative">
                          <input
                            name="zip"
                            className="form-control"
                            id="zip"
                            placeholder="Zip"
                            value={form.zip}
                            onChange={handleChange}
                            required
                          />
                          <span className="input-icon"><i className="fa fa-mail-bulk"></i></span>
                          <label htmlFor="zip">Zip</label>
                        </div>
                      </div>
                      <div className="col-md-6">
                        <div className="form-floating position-relative">
                          <input
                            name="country"
                            className="form-control"
                            id="country"
                            placeholder="Country"
                            value={form.country}
                            onChange={handleChange}
                            required
                          />
                          <span className="input-icon"><i className="fa fa-globe"></i></span>
                          <label htmlFor="country">Country</label>
                        </div>
                      </div>
                    </div>
                    <div className="checkout-section-title">Payment Method</div>
                    <div className="row g-3 mb-4">
                      <div className="col-md-6">
                        <div className="form-floating position-relative">
                          <select
                            name="payment"
                            className="form-select"
                            id="payment"
                            value={form.payment}
                            onChange={handleChange}
                            required
                          >
                            <option value="">Select...</option>
                            <option value="card">Credit/Debit Card</option>
                            <option value="cod">Cash on Delivery</option>
                          </select>
                          <span className="input-icon"><i className="fa fa-credit-card"></i></span>
                          <label htmlFor="payment">Payment</label>
                        </div>
                      </div>
                      {form.payment === "card" && (
                        <div className="col-md-6">
                          <div className="form-floating position-relative">
                            <input
                              type="text"
                              className="form-control"
                              id="cardNumber"
                              name="cardNumber"
                              placeholder="Card Number"
                              value={form.cardNumber}
                              onChange={handleChange}
                              maxLength={19}
                              required
                            />
                            <span className="input-icon"><i className="fa fa-credit-card"></i></span>
                            <label htmlFor="cardNumber">Card Number</label>
                          </div>
                        </div>
                      )}
                    </div>
                    <button
                      type="submit"
                      className="btn btn-danger w-100 py-2 fw-bold"
                      disabled={submitting}
                    >
                      {submitting ? (
                        <>
                          <span className="spinner-border spinner-border-sm me-2"></span>
                          Placing Order...
                        </>
                      ) : (
                        "Place Order"
                      )}
                    </button>
                  </form>
                </div>
              </div>
              <div className="text-center mt-4 text-muted small">
                <i className="fa fa-lock me-2"></i>
                Your information is encrypted and secure.
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Checkout;
