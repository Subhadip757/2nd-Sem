import React, { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet';
import { useNavigate } from 'react-router-dom';

function Confirmation() {
  const [orderDetails, setOrderDetails] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    loadOrderConfirmation();
  }, []);

  const loadOrderConfirmation = () => {
    try {
      const details = JSON.parse(localStorage.getItem('lastOrder'));
      if (!details) {
        navigate('/');
        return;
      }
      setOrderDetails(details);
    } catch (error) {
      console.error('Error loading order confirmation:', error);
      navigate('/');
    }
  };

  const generateOrderNumber = () => {
    const timestamp = Date.now().toString();
    const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
    return `ORD-${timestamp.slice(-6)}${random}`;
  };

  if (!orderDetails) return null;

  const orderNumber = generateOrderNumber();
  const deliveryDate = new Date();
  deliveryDate.setDate(deliveryDate.getDate() + 7);

  return (
    <>
      <Helmet>
        <title>Order Confirmation - ShopEasy</title>
        <style>
          {`
            .confirmation-bg {
              background: linear-gradient(135deg, #f8fafc 0%, #ffe3e3 100%);
              min-height: 100vh;
              padding: 3rem 0;
            }
            .confirmation-card {
              background: #fff;
              border-radius: 18px;
              box-shadow: 0 8px 32px rgba(44,62,80,0.10), 0 1.5px 4px rgba(220,53,69,0.08);
              animation: fadeInUp 0.7s;
              max-width: 700px;
              margin: 0 auto;
              overflow: hidden;
            }
            @keyframes fadeInUp {
              from { opacity: 0; transform: translateY(40px);}
              to { opacity: 1; transform: translateY(0);}
            }
            .confirmation-header {
              background: #dc3545;
              color: white;
              padding: 2.5rem 2rem 2rem 2rem;
              text-align: center;
              border-top-left-radius: 18px;
              border-top-right-radius: 18px;
              position: relative;
            }
            .confirmation-checkmark {
              width: 70px;
              height: 70px;
              border-radius: 50%;
              background: #fff;
              display: flex;
              align-items: center;
              justify-content: center;
              margin: 0 auto 1rem auto;
              box-shadow: 0 2px 8px rgba(0,0,0,0.08);
              animation: pop 0.5s;
            }
            @keyframes pop {
              0% { transform: scale(0.5);}
              80% { transform: scale(1.1);}
              100% { transform: scale(1);}
            }
            .confirmation-header h2 {
              font-weight: 700;
              margin-bottom: 0.5rem;
            }
            .confirmation-body {
              padding: 2.5rem 2rem;
            }
            .confirmation-section-title {
              color: #dc3545;
              font-weight: 600;
              margin-bottom: 1rem;
              font-size: 1.1rem;
              letter-spacing: 0.5px;
            }
            .order-summary-box {
              background: #f8f9fa;
              border-radius: 10px;
              padding: 1.2rem 1rem;
              margin-bottom: 1.5rem;
            }
            .order-summary-box strong {
              color: #dc3545;
            }
            .confirmation-btns {
              display: flex;
              flex-wrap: wrap;
              gap: 1rem;
              justify-content: flex-end;
              margin-top: 2rem;
            }
            .confirmation-btns .btn {
              min-width: 160px;
            }
            @media (max-width: 600px) {
              .confirmation-card { padding: 0; }
              .confirmation-body { padding: 1.2rem 0.5rem; }
              .confirmation-header { padding: 2rem 0.5rem 1.5rem 0.5rem; }
            }
          `}
        </style>
      </Helmet>
      <div className="confirmation-bg">
        <div className="confirmation-card">
          <div className="confirmation-header">
            <div className="confirmation-checkmark">
              <i className="fas fa-check fa-2x text-success"></i>
            </div>
            <h2>Thank you for your order!</h2>
            <div>Your order has been placed successfully.</div>
            <div className="mt-2" style={{ fontSize: "1.1rem" }}>
              <strong>Order #{orderNumber}</strong>
            </div>
          </div>
          <div className="confirmation-body">
            <div className="row g-4">
              <div className="col-md-6">
                <div className="confirmation-section-title">Shipping Details</div>
                <div className="order-summary-box">
                  <div>
                    <strong>Name:</strong> {orderDetails.firstName} {orderDetails.lastName}
                  </div>
                  <div>
                    <strong>Address:</strong> {orderDetails.address}, {orderDetails.city}, {orderDetails.state} {orderDetails.zip}, {orderDetails.country}
                  </div>
                  <div>
                    <strong>Estimated Delivery:</strong> {deliveryDate.toLocaleDateString('en-US', {
                      weekday: 'long',
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric'
                    })}
                  </div>
                </div>
              </div>
              <div className="col-md-6">
                <div className="confirmation-section-title">Payment</div>
                <div className="order-summary-box">
                  <div>
                    <strong>Payment Method:</strong> {orderDetails.payment === "card" ? "Credit/Debit Card" : "Cash on Delivery"}
                  </div>
                  <div>
                    <strong>Order Date:</strong> {new Date().toLocaleDateString()}
                  </div>
                </div>
              </div>
            </div>
            <div className="confirmation-btns">
              <button className="btn btn-outline-danger" onClick={() => window.print()}>
                <i className="fas fa-print me-2"></i>Print Order
              </button>
              <button className="btn btn-outline-secondary" onClick={() => navigate('/')}>
                Continue Shopping
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Confirmation;