import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet';
import { useNavigate } from 'react-router-dom';

function Cart() {
  const navigate = useNavigate();
  const [cartItems, setCartItems] = useState([]);
  const [recentlyViewed, setRecentlyViewed] = useState([]);
  const [promoCode, setPromoCode] = useState('');
  const [appliedPromo, setAppliedPromo] = useState(null);
  const [showPromoCode, setShowPromoCode] = useState(false);
  const [cartSummary, setCartSummary] = useState({
    subtotal: 0,
    shipping: 0,
    tax: 0,
    discount: 0,
    total: 0
  });
  const [showPopup, setShowPopup] = useState(false);

  const SHIPPING_THRESHOLD = 50;
  const BASE_SHIPPING = 5.99;
  const TAX_RATE = 0.10;
  const MAX_QUANTITY = 10;
  const PROMO_CODES = {
    'SAVE20': { discount: 0.20, description: '20% off' },
    'SAVE10': { discount: 0.10, description: '10% off' },
    'WELCOME': { discount: 0.15, description: '15% off for new customers' }
  };

  useEffect(() => {
    loadCart();
    loadRecentlyViewed();
  }, []);

  const loadCart = async () => {
    try {
      const cart = JSON.parse(localStorage.getItem('cart')) || [];
      if (!Array.isArray(cart)) {
        throw new Error('Invalid cart data');
      }

      if (cart.length === 0) {
        setCartItems([]);
        return;
      }

      const items = await Promise.all(
        cart.map(async (item) => {
          try {
            const response = await fetch(`https://dummyjson.com/products/${item.id}`);
            if (!response.ok) throw new Error(`Product ${item.id} not found`);
            const product = await response.json();
            return {
              ...product,
              quantity: item.quantity
            };
          } catch (error) {
            console.error(`Error loading product ${item.id}:`, error);
            return null;
          }
        })
      );

      setCartItems(items.filter(Boolean));
      updateCartSummary(items.filter(Boolean));
    } catch (error) {
      console.error('Error loading cart:', error);
      showToast('Failed to load cart', 'danger');
    }
  };

  const loadRecentlyViewed = () => {
    try {
      const viewed = JSON.parse(localStorage.getItem('recentlyViewed')) || [];
      setRecentlyViewed(viewed);
    } catch (error) {
      console.error('Error loading recently viewed items:', error);
    }
  };

  const updateQuantity = (productId, newQuantity) => {
    if (newQuantity < 1 || newQuantity > MAX_QUANTITY) return;

    try {
      const updatedItems = cartItems.map(item => {
        if (item.id === productId) {
          return { ...item, quantity: newQuantity };
        }
        return item;
      });

      setCartItems(updatedItems);
      updateCartSummary(updatedItems);
      saveCartToLocalStorage(updatedItems);
      window.dispatchEvent(new Event("cartUpdated"));

      if (newQuantity === MAX_QUANTITY) {
        showToast(`Maximum quantity (${MAX_QUANTITY}) reached`, 'info');
      }
    } catch (error) {
      console.error('Error updating quantity:', error);
      showToast('Failed to update quantity', 'danger');
    }
  };

  const removeItem = (productId) => {
    try {
      const updatedItems = cartItems.filter(item => item.id !== productId);
      setCartItems(updatedItems);
      updateCartSummary(updatedItems);
      saveCartToLocalStorage(updatedItems);
      window.dispatchEvent(new Event("cartUpdated"));
      showToast('Item removed from cart', 'success');
    } catch (error) {
      console.error('Error removing item:', error);
      showToast('Failed to remove item', 'danger');
    }
  };

  const updateCartSummary = (items) => {
    try {
      const subtotal = items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
      const shipping = subtotal >= SHIPPING_THRESHOLD ? 0 : BASE_SHIPPING;
      const tax = subtotal * TAX_RATE;
      let discount = 0;

      if (appliedPromo && PROMO_CODES[appliedPromo]) {
        discount = subtotal * PROMO_CODES[appliedPromo].discount;
      }

      const total = subtotal + shipping + tax - discount;

      setCartSummary({
        subtotal,
        shipping,
        tax,
        discount,
        total
      });

      if (subtotal > 0 && subtotal < SHIPPING_THRESHOLD) {
        const remaining = SHIPPING_THRESHOLD - subtotal;
        showToast(`Add $${remaining.toFixed(2)} more for free shipping!`, 'info');
      }
    } catch (error) {
      console.error('Error updating cart summary:', error);
      showToast('Failed to update cart summary', 'danger');
    }
  };

  const saveCartToLocalStorage = (items) => {
    try {
      const cart = items.map(({ id, quantity }) => ({ id, quantity }));
      localStorage.setItem('cart', JSON.stringify(cart));
      window.dispatchEvent(new Event("cartUpdated"));
    } catch (error) {
      console.error('Error saving cart:', error);
    }
  };

  const togglePromoCode = () => {
    setShowPromoCode(!showPromoCode);
    if (!showPromoCode) {
      setPromoCode('');
    }
  };

  const applyPromoCode = () => {
    try {
      if (appliedPromo) {
        localStorage.removeItem('appliedPromoCode');
        setAppliedPromo(null);
        setPromoCode('');
        showToast('Promo code removed', 'success');
        return;
      }

      const code = promoCode.trim().toUpperCase();
      if (!code) {
        showToast('Please enter a promo code', 'danger');
        return;
      }

      if (!PROMO_CODES[code]) {
        showToast('Invalid promo code', 'danger');
        return;
      }

      localStorage.setItem('appliedPromoCode', JSON.stringify(code));
      setAppliedPromo(code);
      showToast(`${PROMO_CODES[code].description} applied successfully!`, 'success');
      updateCartSummary(cartItems);
    } catch (error) {
      console.error('Error applying promo code:', error);
      showToast('Failed to apply promo code', 'danger');
    }
  };

  const saveCartForLater = () => {
    try {
      localStorage.setItem('savedCart', JSON.stringify(cartItems));
      showToast('Cart saved for later', 'success');
    } catch (error) {
      console.error('Error saving cart:', error);
      showToast('Failed to save cart', 'danger');
    }
  };

  const proceedToCheckout = () => {
    try {
      localStorage.setItem('checkoutCart', JSON.stringify({
        items: cartItems,
        summary: cartSummary
      }));
      navigate('/checkout');
    } catch (error) {
      console.error('Error proceeding to checkout:', error);
      showToast('Failed to proceed to checkout', 'danger');
    }
  };

  const showToast = (message, type = 'info') => {
    // Implement toast notification using your preferred toast library
    console.log(`${type}: ${message}`);
  };

  return (
    <>
      <Helmet>
        <title>Shopping Cart - ShopEasy</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
        <style>
          {`
            .quantity-input {
              width: 60px;
              text-align: center;
            }
            .cart-item {
              transition: all 0.3s ease;
            }
            .cart-item:hover {
              background-color: #f8f9fa;
            }
            .remove-item {
              transition: all 0.2s ease;
            }
            .remove-item:hover {
              color: #dc3545 !important;
            }
            .cart-summary {
              position: sticky;
              top: 100px;
            }
            .promo-code {
              max-height: 0;
              overflow: hidden;
              transition: max-height 0.3s ease-out;
            }
            .promo-code.show {
              max-height: 200px;
            }
            .promo-code input.is-invalid {
              border-color: #dc3545;
              animation: shake 0.5s;
            }
            .promo-code input.is-valid {
              border-color: #198754;
            }
            @keyframes shake {
              0%, 100% { transform: translateX(0); }
              25% { transform: translateX(-5px); }
              75% { transform: translateX(5px); }
            }
            #promoCodeBadge {
              font-size: 0.8rem;
              padding: 0.4em 0.8em;
            }
          `}
        </style>
      </Helmet>

      <div className="container py-5">
        <h2 className="mb-4">Your Shopping Cart</h2>
        
        <div className="row">
          {/* Cart Items */}
          <div className="col-lg-8">
            <div className="card shadow-sm">
              <div className="card-body">
                {cartItems.length === 0 ? (
                  <div className="text-center py-5">
                    <i className="fas fa-shopping-cart fa-3x text-muted mb-3"></i>
                    <h4>Your cart is empty</h4>
                    <p className="text-muted">Looks like you haven't added any items yet.</p>
                    <button className="btn btn-danger" onClick={() => navigate('/products')}>
                      Continue Shopping
                    </button>
                  </div>
                ) : (
                  cartItems.map((item) => (
                    <div key={item.id} className="cart-item mb-3 border rounded p-3">
                      <div className="row align-items-center">
                        <div className="col-md-2">
                          <img 
                            src={item.thumbnail} 
                            className="img-fluid rounded" 
                            alt={item.title}
                            onError={(e) => e.target.src = 'placeholder.jpg'}
                          />
                        </div>
                        <div className="col-md-4">
                          <h6 className="mb-1">{item.title}</h6>
                          <small className="text-muted d-block">{item.brand}</small>
                          <p className="text-danger mb-0">${item.price.toFixed(2)}</p>
                        </div>
                        <div className="col-md-3">
                          <div className="quantity-control">
                            <div className="input-group input-group-sm">
                              <button 
                                className="btn btn-outline-secondary"
                                onClick={() => updateQuantity(item.id, item.quantity - 1)}
                                disabled={item.quantity <= 1}
                              >
                                <i className="fas fa-minus"></i>
                              </button>
                              <input 
                                type="number"
                                className="form-control text-center quantity-input"
                                value={item.quantity}
                                min="1"
                                max={MAX_QUANTITY}
                                onChange={(e) => updateQuantity(item.id, parseInt(e.target.value))}
                              />
                              <button 
                                className="btn btn-outline-secondary"
                                onClick={() => updateQuantity(item.id, item.quantity + 1)}
                                disabled={item.quantity >= MAX_QUANTITY}
                              >
                                <i className="fas fa-plus"></i>
                              </button>
                            </div>
                            <small className="text-muted d-block mt-1">Max: {MAX_QUANTITY}</small>
                          </div>
                        </div>
                        <div className="col-md-2 text-end">
                          <span className="fw-bold">
                            ${(item.price * item.quantity).toFixed(2)}
                          </span>
                        </div>
                        <div className="col-md-1 text-end">
                          <button 
                            className="btn btn-link text-danger p-0"
                            onClick={() => removeItem(item.id)}
                          >
                            <i className="fas fa-trash"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  ))
                )}
              </div>
            </div>

            {/* Recently Viewed Items */}
            <div className="mt-4">
              <h4>Recently Viewed</h4>
              <div className="row g-3">
                {recentlyViewed.length === 0 ? (
                  <p className="text-muted">No recently viewed items</p>
                ) : (
                  recentlyViewed.map((product) => (
                    <div key={product.id} className="col-md-3">
                      <div className="card h-100">
                        <img 
                          src={product.thumbnail} 
                          className="card-img-top p-2" 
                          alt={product.title}
                          style={{ height: '150px', objectFit: 'contain' }}
                        />
                        <div className="card-body">
                          <h6 className="card-title">{product.title}</h6>
                          <p className="card-text text-danger">${product.price}</p>
                          <button 
                            className="btn btn-sm btn-outline-danger w-100"
                            onClick={() => navigate(`/product/${product.id}`)}
                          >
                            Add to Cart
                          </button>
                        </div>
                      </div>
                    </div>
                  ))
                )}
              </div>
            </div>
          </div>

          {/* Cart Summary */}
          <div className="col-lg-4">
            <div className="card shadow-sm cart-summary">
              <div className="card-body">
                <h5 className="card-title mb-4">Order Summary</h5>
                
                {/* Price Details */}
                <div className="d-flex justify-content-between mb-2">
                  <span>Subtotal</span>
                  <span>${cartSummary.subtotal.toFixed(2)}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Shipping</span>
                  <span>{cartSummary.shipping === 0 ? 'FREE' : `$${cartSummary.shipping.toFixed(2)}`}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Tax (10%)</span>
                  <span>${cartSummary.tax.toFixed(2)}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Discount</span>
                  <span className="text-danger">-${cartSummary.discount.toFixed(2)}</span>
                </div>
                <hr />
                <div className="d-flex justify-content-between mb-4">
                  <strong>Total</strong>
                  <strong>${cartSummary.total.toFixed(2)}</strong>
                </div>

                {/* Promo Code */}
                <div className="mb-3">
                  <button 
                    className="btn btn-outline-secondary w-100 mb-2"
                    onClick={togglePromoCode}
                  >
                    <i className="fas fa-tag me-2"></i>Add Promo Code
                  </button>
                  <div className={`promo-code ${showPromoCode ? 'show' : ''}`}>
                    <div className="input-group mt-2">
                      <input 
                        type="text"
                        className="form-control"
                        value={promoCode}
                        onChange={(e) => setPromoCode(e.target.value)}
                        placeholder="Enter promo code"
                        disabled={!!appliedPromo}
                      />
                      <button 
                        className="btn btn-outline-danger"
                        onClick={applyPromoCode}
                      >
                        {appliedPromo ? 'Remove' : 'Apply'}
                      </button>
                    </div>
                    <div className="d-flex justify-content-between align-items-center mt-2">
                      <small className="text-muted">Available codes: SAVE20, SAVE10, WELCOME</small>
                      {appliedPromo && (
                        <span className="badge bg-success" id="promoCodeBadge">
                          {PROMO_CODES[appliedPromo].description} applied
                        </span>
                      )}
                    </div>
                  </div>
                </div>

                {/* Checkout Button */}
                <button 
                  className="btn btn-danger w-100 mb-3"
                  onClick={proceedToCheckout}
                  disabled={cartItems.length === 0}
                >
                  Proceed to Checkout
                </button>

                {/* Save for Later */}
                <button 
                  className="btn btn-outline-secondary w-100"
                  onClick={saveCartForLater}
                  disabled={cartItems.length === 0}
                >
                  Save Cart for Later
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {showPopup && (
        <div
          style={{
            position: "fixed",
            top: "20px",
            right: "20px",
            background: "#dc3545",
            color: "#fff",
            padding: "1em 2em",
            borderRadius: "8px",
            zIndex: 9999,
            boxShadow: "0 2px 8px rgba(0,0,0,0.2)",
          }}
        >
          Added to cart!
        </div>
      )}
    </>
  );
}

export default Cart;